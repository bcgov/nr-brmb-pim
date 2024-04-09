import {
    AfterViewInit, ChangeDetectorRef,
    Directive,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    Output,
    Renderer2,
    SimpleChanges
} from "@angular/core";
import {ApplicationStateService} from "../services/application-state.service";
import {arrayEquals} from "../utils";

declare var $: any;

@Directive({
    selector: '[appWFSingleSelect]'
})
export class SingleSelectDirective implements AfterViewInit, OnChanges {
    @Input() appWFPlaceholder?: string;
    @Output() updated: EventEmitter<any> = new EventEmitter();
    @Input() options: any[];
    @Input() selected: any;
    selectHtmlElement: HTMLSelectElement;
    multiselect;

    constructor(
        private element: ElementRef,
        private cdr: ChangeDetectorRef) {
    }

    ngAfterViewInit() {
        this.selectHtmlElement = this.element.nativeElement;
        let self = this;
        this.selectHtmlElement.addEventListener("change", function () {
            self.onClick();
        });
        this.multiselect = $(this.selectHtmlElement).multipleSelect({
            placeholder: this.appWFPlaceholder ? this.appWFPlaceholder : "Select...",
            filter: true,
            onClick: this.onClick.bind(this),
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.options && !arrayEquals(changes.options.currentValue, changes.options.previousValue)) {
            this.options = changes.options.currentValue;
            setTimeout(() => {
                this.cdr.detectChanges();
                this.multiselect.multipleSelect("refresh");
            });
        }
        if (changes.selected) {
            setTimeout(() => {
                this.selected = changes.selected.currentValue;
                this.multiselect.multipleSelect("setSelects", [this.selected]);
            });
        }
    }

    onClick() {
        let selected = "";
        if (this.selectHtmlElement.selectedOptions && this.selectHtmlElement.selectedOptions.length) {
            selected = this.selectHtmlElement.selectedOptions.item(0).value;
        }
        this.updated.emit(selected);
    }
}
