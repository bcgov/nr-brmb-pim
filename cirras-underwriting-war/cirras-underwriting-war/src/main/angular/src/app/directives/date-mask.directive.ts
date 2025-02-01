import {Directive, ElementRef, HostListener, Input, OnDestroy} from "@angular/core";
import * as textMask from "vanilla-text-mask/dist/vanillaTextMask.js";
import {CONSTANTS} from "../utils";
import {UntypedFormControl} from "@angular/forms";
import {Subscription} from "rxjs";
// import * as moment from "moment";
import moment, { Moment } from "moment";

@Directive({
  selector: '[appWFDateMask]'
})
export class DateMaskDirective implements OnDestroy {
  @Input() public formControl: UntypedFormControl;
  public lastValidDate: string;
  public formControlValueSub: Subscription;
  dateInputElement: HTMLInputElement;

  mask = CONSTANTS.DATE_MASK;
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
      if (val && val.length == 10) {
        let m = moment(event.target.value);
        //console.log("kp", event.target.value, m);
        if (m.isValid()) {
          this.lastValidDate = event.target.value;
          this.formControl.setValue(this.lastValidDate);
        }
      }
    }
  }

  ngOnDestroy() {
    this.dateInputController.destroy();
  }
}
