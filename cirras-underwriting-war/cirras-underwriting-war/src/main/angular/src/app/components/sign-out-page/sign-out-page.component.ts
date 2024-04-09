import {Component, OnDestroy, OnInit} from "@angular/core";
import {BaseComponent} from "../common/base/base.component";

@Component({
    selector: "cirras-underwriting-sign-out-page",
    templateUrl: "./sign-out-page.component.html",
    styleUrls: ["./sign-out-page.component.scss"]
})
export class SignOutPageComponent extends BaseComponent implements OnInit, OnDestroy {
    ngOnInit() {
        localStorage.removeItem('oauth');
        const appConfig = (<any>this.appConfigService.getConfig()).application;
        const webadeConfig = (<any>this.appConfigService.getConfig()).webade;
        const siteminderUrlPrefix = webadeConfig.siteminderUrlPrefix
        let url = siteminderUrlPrefix + appConfig.baseUrl + "&retnow=1";
        window.location.href = url;
    }

    ngOnDestroy() {

    }

}
