import {
    ChangeDetectorRef,
    Component,
    HostListener,
    Input,
} from "@angular/core";
import {Router} from "@angular/router";
import {ErrorState} from "../../../store/application/application.state";
import {ApplicationStateService} from "../../../services/application-state.service";
import {Store} from "@ngrx/store";
import {RootState} from "../../../store";
import {SecurityUtilService} from "../../../services/security-util.service";
import { UwContract } from "src/app/conversion/models";

export interface NavItem {
    icon?: any;
    svgIcon?: any;
    label: string;
    routerLink: string;
    badge: number;
    queryParams?: any;
    disabled?: boolean;
}

export interface ActionItem {
    label: string;
    callBackFunction: Function;
}

@Component({
    selector: "base-wrapper",
    templateUrl: "./base-wrapper.component.html",
    styleUrls: ["./base-wrapper.component.scss"],
})

export class BaseWrapperComponent {

    @Input() errorState?: ErrorState[];

    isMobileRes = false;


    constructor(private router: Router,
                private applicationStateService: ApplicationStateService,
                private securityUtilService: SecurityUtilService,
                private store: Store<RootState>,
                private cdr: ChangeDetectorRef) {

    }

    @HostListener("window:resize", ["$event"])
    onResize(event) {
        this.isMobileRes = this.applicationStateService.getIsMobileResolution();
        this.cdr.detectChanges();
    }
}

