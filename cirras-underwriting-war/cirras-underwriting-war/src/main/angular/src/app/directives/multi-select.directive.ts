import {
    AfterViewInit,
    ChangeDetectorRef,
    Directive,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    Output,
    SimpleChanges
} from "@angular/core";
import {arrayEquals} from "../utils";

declare var $: any;

@Directive({
    selector: '[appWFMultiSelect]',
    standalone: false
})
export class MultiSelectDirective implements AfterViewInit, OnChanges {
    @Input() appWFPlaceholder?: string;
    @Input() options: any[];
    @Input() selected: any[];
    @Output() updated: EventEmitter<any> = new EventEmitter();
    selectHtmlElement: HTMLSelectElement;
    multiselect;

    constructor(
        private element: ElementRef,
        private cdr: ChangeDetectorRef) {
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.options && !arrayEquals(changes.options.currentValue, changes.options.previousValue)) {
            this.options = changes.options.currentValue;
            setTimeout(() => {
                this.cdr.detectChanges();
                this.multiselect.multipleSelect("refresh");
            });

        }
        if (changes.selected && !arrayEquals(changes.selected.currentValue, changes.selected.previousValue)) {
            setTimeout(() => {
                this.selected = changes.selected.currentValue;
                this.multiselect.multipleSelect("setSelects", this.selected);
            });
        }
    }

    ngAfterViewInit() {
        this.selectHtmlElement = this.element.nativeElement;
        let self = this;
        this.selectHtmlElement.addEventListener("change", function () {
            self.onClick();
        });
        this.multiselect = $(this.selectHtmlElement).multipleSelect({
            placeholder: this.appWFPlaceholder ? this.appWFPlaceholder : "Select...",
            minimumCountSelected: 1,
            onClick: this.onClick.bind(this),
            onCheckAll: this.checkAll.bind(this),
            onUncheckAll: this.uncheckAll.bind(this),
            filter: true,
            selectAll: true,
        });
    }

    onClick() {
        let selected = [];
        for (let i = 0; i < this.selectHtmlElement.selectedOptions.length; i++) {
            selected.push(this.selectHtmlElement.selectedOptions.item(i).value);
        }
        this.updated.emit(selected);
    }

    uncheckAll() {
        this.updated.emit([]);
    }

    checkAll() {
        this.updated.emit(this.options.map(item => item.code));
    }
}
