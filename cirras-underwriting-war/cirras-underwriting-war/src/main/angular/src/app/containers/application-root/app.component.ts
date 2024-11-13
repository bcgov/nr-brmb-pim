import {ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnInit} from "@angular/core";
import {UpdateService} from "../../services/update.service";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {RootState} from "../../store";
import {DomSanitizer} from "@angular/platform-browser";
import {MatIconRegistry} from "@angular/material/icon";
import {ApplicationStateService} from "../../services/application-state.service";
import {addRemoveCdkOverlayClass} from "../../utils";
import { RouterLink, WfApplicationConfiguration, WfApplicationState, WfDevice } from "@wf1/wfcc-application-ui";
import {Subscription} from "rxjs";
import { ResourcesRoutes } from "src/app/utils/constants";
import { MatDialog } from "@angular/material/dialog";
import { AppConfigService, TokenService } from "@wf1/wfcc-core-lib";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { ROUTE_SCOPES, SCOPES_UI } from "src/app/utils/scopes";

const DEVICE: WfDevice = 'desktop';

@Component({
    selector: "cirras-underwriting-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit {

    applicationState: WfApplicationState = {
        menu: 'expanded'
    };

    appMenu: RouterLink[] = [];

    applicationConfig: WfApplicationConfiguration = {
        title: "CIRRAS GRAIN AND FORAGE UNDERWRITING",
        device: DEVICE,
        userName: "",
        version: {
            long: "",
            short: ""
        },
        environment: ""
    };

    appConfigSubscription: Subscription;
    tokenSubscription: Subscription;

    constructor(private updateService: UpdateService,
                private router: Router, private store: Store<RootState>,
                protected cdr: ChangeDetectorRef,
                protected dialog: MatDialog,
                protected tokenService: TokenService,
                protected securityUtilService: SecurityUtilService,
                protected appConfigService: AppConfigService,
                protected applicationStateService: ApplicationStateService,
                private matIconRegistry: MatIconRegistry,
                private domSanitizer: DomSanitizer) {
        this.updateService.checkForUpdates();

        this.setAppProperties();

        this.matIconRegistry.addSvgIcon(
            "filter-cancel",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/filter-cancel.svg")
        );

        this.matIconRegistry.addSvgIcon(
            "field",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/field.svg")
        );

        this.matIconRegistry.addSvgIcon(
            "tractor",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/tractor.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "unseeded",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/unseeded.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "seeded",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/seeded.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "dop",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/dop.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "left-panel-close",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/left-panel-close.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "left-panel-open",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/left-panel-open.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "visibility",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/visibility.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "visibility_off",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/visibility_off.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "cancel",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/cancel.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "mode_comment",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/mode_comment.svg")
        );
        this.matIconRegistry.addSvgIcon(
            "insert_comment",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/insert_comment.svg")
        );

    }

    setAppProperties() {
        let vh = window.innerHeight * 0.01;
        document.documentElement.style.setProperty("--vh", `${vh}px`);

        let vw = window.innerWidth * 0.01;
        document.documentElement.style.setProperty("--vw", `${vw}px`);

        document.documentElement.style.setProperty("--cirras-underwriting-gutter-space", this.applicationStateService.getIsMobileResolution() ? "8px" : "16px");
        addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());

        this.initMainNavItems();

    }

    @HostListener("window:orientationchange", ["$event"])
    onOrientationChange(event) {
        setTimeout(() => {
            let vh = window.innerHeight * 0.01;
            document.documentElement.style.setProperty("--vh", `${vh}px`);

            let vw = window.innerWidth * 0.01;
            document.documentElement.style.setProperty("--vw", `${vw}px`);

            addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());
            document.documentElement.style.setProperty("--cirras-underwriting-gutter-space", this.applicationStateService.getIsMobileResolution() ? "8px" : "16px");
        }, 250);
    }

    @HostListener("window:resize", ["$event"])
    onResize(event) {
        setTimeout(() => {
            let vh = window.innerHeight * 0.01;
            document.documentElement.style.setProperty("--vh", `${vh}px`);

            let vw = window.innerWidth * 0.01;
            document.documentElement.style.setProperty("--vw", `${vw}px`);

            addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());
            document.documentElement.style.setProperty("--cirras-underwriting-gutter-space", this.applicationStateService.getIsMobileResolution() ? "8px" : "16px");

        }, 250);
    }

    ngOnInit(): void {
        
        this.appConfigSubscription = this.appConfigService.configEmitter.subscribe((config) => {

            this.applicationConfig.version.short = config.application.version.replace(/-SNAPSHOT/i, '');
            this.applicationConfig.version.long = config.application.version.replace(/-SNAPSHOT/i, '');
            this.applicationConfig.environment = config.application.environment.replace(/^.*prod.*$/i, '');

            this.applicationConfig.device =  'desktop'; // this.applicationStateService.getIsMobileResolution() ? 'mobile' : 'desktop';
        });
        this.tokenSubscription = this.tokenService.credentialsEmitter.subscribe( (creds) => {
            let first = creds.given_name || creds.givenName;
            let last = creds.family_name || creds.familyName;

            this.applicationConfig.userName = `${ first } ${ last }`;
            this.initMainNavItems();
        } );

        //window[ 'SPLASH_SCREEN' ].remove();
    }

    ngOnDestroy() {
        if (this.appConfigSubscription) {
            this.appConfigSubscription.unsubscribe();
        }
        if (this.tokenSubscription) {
            this.tokenSubscription.unsubscribe();
        }
    }

    initMainNavItems(): RouterLink[] {
        let items: RouterLink[] = [];
        
        items.push(new RouterLink("Policy",
            "/" + ResourcesRoutes.LANDING,
            "text_snippet",
            this.applicationConfig.device == 'desktop' ? null : 'hidden',
            this.router,
            true));

            if (this.securityUtilService.hasScopes(ROUTE_SCOPES.LEGAL_LAND)) {
                items.push(new RouterLink("Legal Land",
                    "/" + ResourcesRoutes.LAND_LIST,
                    "landscape",
                    this.applicationConfig.device == 'desktop' ? null : 'hidden',
                    this.router,
                    true));
            }
            if (this.securityUtilService.hasScopes(ROUTE_SCOPES.MAINTAIN)) {
                items.push(new RouterLink("Support Data",
                    "/" + ResourcesRoutes.MAINTAIN_DASHBOARD,
                    "bar_chart",
                    this.applicationConfig.device == 'desktop' ? null : 'hidden',
                    this.router,
                    true));
            }
            if (this.securityUtilService.hasScopes(ROUTE_SCOPES.MAINTAIN_GRADE_MODIFIERS)) {
                items.push(new RouterLink('Annual Grade \n Adjustments',
                    "/" + ResourcesRoutes.MAINTAIN_GRADE_MODIFIERS,
                    "grading",
                    this.applicationConfig.device == 'desktop' ? null : 'hidden',
                    this.router,
                    true,
                    true
                ));
            }
            if ( this.securityUtilService.hasScopes(ROUTE_SCOPES.MAINTAIN_SEEDING_DEADLINES)) {
                items.push(new RouterLink("Annual Seeding \n Deadlines",
                    "/" + ResourcesRoutes.MAINTAIN_SEEDING_DEADLINES,
                    "calendar_today",
                    this.applicationConfig.device == 'desktop' ? null : 'hidden',
                    this.router,
                    true,
                    true
                ));
            }
            if ( this.securityUtilService.hasScopes(ROUTE_SCOPES.MAINTAIN_FORAGE_VARIETY_INSURABILITY)) {
                items.push(new RouterLink("Forage Variety \n Insurability",
                    "/" + ResourcesRoutes.MAINTAIN_FORAGE_VARIETY_INSURABILITY,
                    "verified_user",
                    this.applicationConfig.device == 'desktop' ? null : 'hidden',
                    this.router,
                    true,
                    true
                ));
            }

            if ( this.securityUtilService.hasScopes(ROUTE_SCOPES.MAINTAIN_YIELD_CONVERSION)) {
                items.push(new RouterLink("Yield Conversion",
                    "/" + ResourcesRoutes.MAINTAIN_YIELD_CONVERSION,
                    "published_with_changes",
                    this.applicationConfig.device == 'desktop' ? null : 'hidden',
                    this.router,
                    true,
                    true
                ));
            }

    
        this.appMenu = items;
        return items;
    }
}
