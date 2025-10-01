import {Directive, ElementRef, HostListener, Input, OnChanges, OnDestroy, SimpleChanges} from "@angular/core";
import {CONSTANTS} from "../utils";
import {UntypedFormControl} from "@angular/forms";
import {Subscription} from "rxjs";

@Directive({
    selector: '[appWFTimeMask]',
    standalone: false
})
export class TimeMaskDirective implements OnDestroy, OnChanges {
  mask = CONSTANTS.DATE_MASK;
  timeInputElement: any;
  @Input()
  public formControl: UntypedFormControl;
  public formControlValueSub: Subscription;
  public lastValidDate: string;
  constructor(private element: ElementRef) {
    this.timeInputElement = this.element.nativeElement;
  }

  @HostListener('blur', ['$event'])
  onBlur(event) {
    if (this.formControl) {
      if (this.lastValidDate && this.lastValidDate != this.formControl.value) {
        this.formControl.setValue(this.lastValidDate);
      }
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.formControl && !this.formControlValueSub) {
      this.lastValidDate = this.formControl.value;
      this.formControlValueSub = this.formControl.valueChanges.subscribe((value) => {
        if (value) {
          let length = value.length;
          if (length == 4) {
            this.lastValidDate = this.formatThreeDigitTime(value);
          } else if (length < 4) {
            setTimeout(() => {
              this.timeInputElement.setSelectionRange(length, length);
            });
            this.lastValidDate = undefined;
          } else {
            let v1 = this.prepareCompareString(this.lastValidDate);
            let v2 = this.prepareCompareString(value);
            if (v2.length > v1.length) {
              this.lastValidDate = value;
            } else {
              let diffCount = this.diffCount(this.lastValidDate, value);
              if ( diffCount == 1 || diffCount == 0) {
                this.lastValidDate = value;
              }
            }
          }
        }
      });
    }
  }

  prepareCompareString(value) {
    if (value) {
      value = value.replace(':', '' );
      value = parseInt(value).toString();
    }
    return value;
  }

  diffCount(v1: string, v2: string): number {
    if (v1 && v2 && v1.length > 0 && v2.length > 0) {
      let diffCount = 0;
      if (v1.length > v2.length) {
        diffCount += v1.length - v2.length;
      } else if (v2.length > v1.length) {
        diffCount += v2.length - v1.length;
      }
      for (let i = 0; i < v1.length; i++) {
        if (v1[i] !== v2[i]) {
          diffCount++;
        }
      }
      return diffCount;
    } else {
      if (v1) {
        return v1.length;
      } else if (v2) {
        return v2.length;
      } else {
        return 0;
      }
    }
  }

  formatThreeDigitTime(value) {
    if (!value) { return value; }

    let timeValue = value;
    if (timeValue.length == 4) {
      timeValue = `0${timeValue}`.replace(':', '');
      timeValue = `${timeValue.slice(0, 2)}:${timeValue.slice(2, 4)}`;
    }
    return timeValue;
  }

  ngOnDestroy() {
    if (this.formControlValueSub) {
      this.formControlValueSub.unsubscribe();
    }
  }
}
