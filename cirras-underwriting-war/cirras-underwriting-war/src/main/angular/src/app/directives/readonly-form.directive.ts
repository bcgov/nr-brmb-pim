import {AfterViewInit, Directive, ElementRef, Input, Renderer2} from "@angular/core";
import {makeFormFieldReadonly} from "../utils";

@Directive({
  selector: '[appWFReadonlyForm]'
})
export class ReadonlyFormDirective implements AfterViewInit {
  @Input() appWFReadonlyForm: string;
  formHtmlElement: HTMLElement;

  constructor(
      private element: ElementRef,
      private renderer: Renderer2) {
  }

  ngAfterViewInit() {
    if (this.appWFReadonlyForm) {
      this.formHtmlElement = this.element.nativeElement;
      let associatedFormElements = this.formHtmlElement.getElementsByTagName('mat-form-field');
      if (associatedFormElements && associatedFormElements.length) {
        for (let i = 0; i < associatedFormElements.length; i++) {
          let formEl = associatedFormElements.item(i) as HTMLElement;
          makeFormFieldReadonly(this.renderer, formEl);
        }
      }
    }

  }
}
