import {Directive, ElementRef, HostListener, Input, OnDestroy} from "@angular/core";
import * as textMask from "vanilla-text-mask/dist/vanillaTextMask.js";
import {CONSTANTS} from "../utils";
import {FormControl} from "@angular/forms";
import {Subscription} from "rxjs";

@Directive({
  selector: '[appWFDateRangeMask]'
})
export class DateRangeMaskDirective implements OnDestroy {
  @Input() public formControl: FormControl;
  public lastValidDate: string;
  public formControlValueSub: Subscription;
  dateInputElement: HTMLInputElement;

  mask = CONSTANTS.DATE_RANGE_MASK;
  dateInputController;

  constructor(private element: ElementRef) {
    this.dateInputElement = this.element.nativeElement;
    this.dateInputController = textMask.maskInput({
      inputElement: this.element.nativeElement,
      mask: this.mask,
      guide: false
    });
  }

  @HostListener('keyup', ['$event'])
  onKeypress(event) {
    if (this.formControl) {
      let val = event.target.value;
      if (val && val.length == 23) {
        //let m = moment(event.target.value);
        //console.log("kp", event.target.value, m);
        //if (m.isValid()) {
          this.lastValidDate = event.target.value;
          this.formControl.setValue(this.lastValidDate);
        //}
      }
    }
  }

  ngOnDestroy() {
    this.dateInputController.destroy();
  }
}
