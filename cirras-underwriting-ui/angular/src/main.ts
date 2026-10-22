import {enableProdMode} from "@angular/core";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";

import {AppModule} from "./app/app.module";
import {environment} from "./environments/environment";

if (environment.production) {
    enableProdMode();
}

//let isIEOrEdge = /msie\s|trident\//i.test(window.navigator.userAgent);
let isIEOrEdge = false;
let isFirefox = navigator.userAgent.toLowerCase().indexOf("firefox") > -1;

if (!isIEOrEdge) {
    platformBrowserDynamic().bootstrapModule(AppModule).then(() => {
        if ("serviceWorker" in navigator && environment.production) {
            navigator.serviceWorker.register("ngsw-worker.js");
        }

    }).catch(err => console.log(err));
}


