import {ChangeDetectorRef, Directive, ElementRef, Input, OnChanges, Renderer2, SimpleChanges} from "@angular/core";
import {makeFormFieldReadonly} from "../utils";

@Directive({
    selector: '[appWFReadonlyField]',
    standalone: false
})
export class ReadonlyFieldDirective implements OnChanges {
  @Input() appWFReadonlyField: string;
  formFieldHtmlElement: HTMLElement;

  constructor(
      private element: ElementRef,
      private renderer: Renderer2) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.appWFReadonlyField) {
      this.appWFReadonlyField = changes.appWFReadonlyField.currentValue;
    }
    if (this.appWFReadonlyField) {
      this.formFieldHtmlElement = this.element.nativeElement;
      makeFormFieldReadonly(this.renderer, this.formFieldHtmlElement);
    }
  }

}
