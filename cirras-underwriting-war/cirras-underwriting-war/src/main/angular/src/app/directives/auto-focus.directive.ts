import {Directive, ElementRef, Input} from "@angular/core";

@Directive({
    selector: "[appAutoFocus]",
    standalone: false
})
export class AutoFocusDirective {

    @Input() public autoFocus: boolean;

    public constructor(private el: ElementRef) {
        setTimeout(() => {
            let nativeEl = this.el.nativeElement as HTMLElement;
            this.el.nativeElement.focus();
        }, 100);
    }

}
