import { enableProdMode, APP_INITIALIZER, Injector, importProvidersFrom } from "@angular/core";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";

import { cirrasUnderwritingRestInitializerFn } from "./app/app.module";
import {environment} from "./environments/environment";
import { provideBootstrapEffects, DATE_FORMATS } from "./app/utils";
import { rootEffects, rootReducers, initialRootState } from "./app/store";
import { UpdateService } from "./app/services/update.service";
import { AppConfigService, TokenService, CoreUIModule } from "@bcgov/pit-common-core-lib";
import { Configuration as CirrasUnderwritingAPIServiceConfiguration, ApiModule as CirrasUnderwritingAPIServiceModule } from "@cirras/cirras-underwriting-api";
import { Title, BrowserModule, bootstrapApplication } from "@angular/platform-browser";
import { appInitFn } from "./app/utils/app-initializer";
import { HttpHandler, HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import { OWL_DATE_TIME_FORMATS, OwlDateTimeModule, OwlMomentDateTimeModule } from "@busacca/ng-pick-datetime";
import { RouteReuseStrategy } from "@angular/router";
import { CustomReuseStrategy } from "./app/utils/custom-route-reuse-strategy";
import { ResourcesInterceptor } from "./app/interceptors/resources-interceptor";
import { ConnectionServiceOptionsToken, ConnectionServiceOptions } from "ngx-connection-service";
import { DateAdapter, MAT_DATE_LOCALE, MAT_DATE_FORMATS, MatNativeDateModule } from "@angular/material/core";
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from "@angular/material-moment-adapter";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule } from "@angular/material/form-field";
import { provideNgxMask, NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { DecimalPipe } from "@angular/common";
import { DragDropModule } from "@angular/cdk/drag-drop";
import { CdkTableModule } from "@angular/cdk/table";
import { provideAnimations } from "@angular/platform-browser/animations";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatBadgeModule } from "@angular/material/badge";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDialogModule } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatMenuModule } from "@angular/material/menu";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTableModule } from "@angular/material/table";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MomentModule } from "ngx-moment";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { StoreDevtoolsModule } from "@ngrx/store-devtools";
import { StoreModule } from "@ngrx/store";
import { AppRoutingModule } from "./app/app-routing.module";
import { NgxPaginationModule } from "ngx-pagination";
import { EffectsModule } from "@ngrx/effects";
import { ServiceWorkerModule } from "@angular/service-worker";
import { A11yModule } from "@angular/cdk/a11y";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { PitApplicationModule } from "@bcgov/pit-common-application-lib";
import { AppComponent } from "./app/containers/application-root/app.component";

const apiConfiguration = new CirrasUnderwritingAPIServiceConfiguration();
let devOnlyImports = [];



if (environment.production) {
    enableProdMode();
}

//let isIEOrEdge = /msie\s|trident\//i.test(window.navigator.userAgent);
let isIEOrEdge = false;
let isFirefox = navigator.userAgent.toLowerCase().indexOf("firefox") > -1;

if (!isIEOrEdge) {
    bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(CirrasUnderwritingAPIServiceModule, DragDropModule, CdkTableModule, BrowserModule, FormsModule, MatExpansionModule, MatBadgeModule, MatGridListModule, MatAutocompleteModule, MatButtonModule, MatCardModule, MatCheckboxModule, MatDialogModule, MatFormFieldModule, MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatRadioModule, MatSelectModule, MatSnackBarModule, MatSortModule, MatTabsModule, MatTableModule, MatPaginatorModule, MatTooltipModule, MatSnackBarModule, MatSidenavModule, MatToolbarModule, MomentModule, MatProgressSpinnerModule, NgxMaskDirective, NgxMaskPipe, OwlDateTimeModule, OwlMomentDateTimeModule, ReactiveFormsModule, ScrollingModule, CoreUIModule.forRoot({ configurationPath: environment.app_config_location }), StoreModule.forRoot(rootReducers, { initialState: initialRootState }), AppRoutingModule, NgxPaginationModule, EffectsModule.forRoot([]), ServiceWorkerModule.register("ngsw-worker.js", { enabled: environment.production, scope: "./" }), ...devOnlyImports, A11yModule, MatNativeDateModule, MatDatepickerModule, PitApplicationModule.forRoot() // ,
        // WildfireResourceManagerModule.forRoot()
        ),
        // Added provideBootstrapEffects function to handle the ngrx issue that loads effects before APP_INITIALIZER
        // providers have finished initializing.
        // See https://github.com/ngrx/platform/issues/931 for more information.
        provideBootstrapEffects(rootEffects),
        UpdateService,
        AppConfigService,
        TokenService,
        Title,
        {
            provide: APP_INITIALIZER,
            useFactory: appInitFn,
            multi: true,
            deps: [HttpHandler, Injector]
        },
        {
            provide: CirrasUnderwritingAPIServiceConfiguration,
            useFactory: cirrasUnderwritingRestInitializerFn,
            multi: false,
            deps: [AppConfigService]
        },
        { provide: OWL_DATE_TIME_FORMATS, useValue: DATE_FORMATS },
        { provide: RouteReuseStrategy, useClass: CustomReuseStrategy },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ResourcesInterceptor,
            multi: true
        },
        {
            provide: ConnectionServiceOptionsToken,
            useValue: <ConnectionServiceOptions>{
                enableHeartbeat: false
            }
        },
        {
            provide: DateAdapter,
            useClass: MomentDateAdapter,
            deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
        },
        { provide: MAT_DATE_FORMATS,
            useValue: {
                parse: {
                    dateInput: 'LL'
                },
                display: {
                    dateInput: 'MMM DD, YYYY',
                    monthYearLabel: 'MMM YYYY',
                    dateA11yLabel: 'LL',
                    monthYearA11yLabel: 'MMM YYYY'
                }
            } },
        {
            provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
            useValue: {
                subscriptSizing: 'dynamic'
            }
        },
        provideNgxMask(),
        DecimalPipe,
        provideHttpClient(withInterceptorsFromDi()),
        provideAnimations()
    ]
}).then(() => {
        if ("serviceWorker" in navigator && environment.production) {
            navigator.serviceWorker.register("ngsw-worker.js");
        }

    }).catch(err => console.log(err));
}


