import {Directive, ElementRef, OnDestroy} from "@angular/core";
import * as textMask from "vanilla-text-mask/dist/vanillaTextMask.js";
import {CONSTANTS} from "../utils";

@Directive({
  selector: '[appWFDateTimeMask]'
})
export class DateTimeMaskDirective implements OnDestroy {

  mask = CONSTANTS.DATE_TIME_MASK;
  dateTimeInputController;

  constructor(private element: ElementRef) {
    this.dateTimeInputController = textMask.maskInput({
      inputElement: this.element.nativeElement,
      mask: this.mask,
      guide: false
    });
  }

  ngOnDestroy() {
    this.dateTimeInputController.destroy();
  }
}
