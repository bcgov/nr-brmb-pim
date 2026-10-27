import {NgModule} from "@angular/core";
import {Router, RouterModule, Routes} from "@angular/router";
import {UnauthorizedPageComponent} from "./components/unauthorized-page/unauthorized-page.component";
import {ApplicationStateService} from "./services/application-state.service";
import {ResourcesRoutes as R} from "./utils/constants";
import {ResourcesAuthGuard} from "./services/util/ResourcesAuthGuard";
import {SignOutPageComponent} from "./components/sign-out-page/sign-out-page.component";
import {ROUTE_SCOPES} from "./utils/scopes";
import { InventoryContractContainer } from "./containers/inventory/inventory-contract-container.component";
import { UwContractsListContainer } from "./containers/uw-contracts-list/uw-contracts-list-container.component";
import { GrainDopContainer } from "./containers/dop/grain-dop-container.component";
import { LandingPageComponent } from "./components/common/landingpage/landingpage.component";
import { DeactivateGuard } from "./services/util/DeactivateGuard";
import { ForageDopContainer } from "./containers/dop/forage-dop-container.component";
import { UserSettingsContainer } from "./containers/maintenance/user-settings-container";

const routesDesktop: Routes = [
    { path: R.LANDING, component: UwContractsListContainer, data: {scopes: [ROUTE_SCOPES.LANDING]},  pathMatch: "full" },
    { path: R.INVENTORY_GRAIN_UNSEEDED, children: [
        { 
          path: ':insurancePlanId/:cropYear/:policyId/:inventoryContractGuid', component: InventoryContractContainer, data: {scopes: [ROUTE_SCOPES.INVENTORY]},
          canActivate: [ResourcesAuthGuard],
          canDeactivate: [DeactivateGuard]
        },
      ] },
    
    { path: R.INVENTORY_GRAIN_SEEDED, children: [
        { 
          path: ':insurancePlanId/:cropYear/:policyId/:inventoryContractGuid', component: InventoryContractContainer , data: {scopes: [ROUTE_SCOPES.INVENTORY]},
          canActivate: [ResourcesAuthGuard],
          canDeactivate: [DeactivateGuard]
        },
      ] },

    { path: R.INVENTORY_FORAGE, 
        children: [
        { 
          path: ':insurancePlanId/:cropYear/:policyId/:inventoryContractGuid', component: InventoryContractContainer, data: {scopes: [ROUTE_SCOPES.INVENTORY]},
          canActivate: [ResourcesAuthGuard],
          canDeactivate: [DeactivateGuard]
        },
        ] 
      },
    
      { path: R.INVENTORY_BERRIES, 
        children: [
        { 
          path: ':insurancePlanId/:cropYear/:policyId/:inventoryContractGuid', component: InventoryContractContainer, data: {scopes: [ROUTE_SCOPES.INVENTORY]},
          canActivate: [ResourcesAuthGuard],
          canDeactivate: [DeactivateGuard]
        },
        ] 
      },

    { path: R.DOP_GRAIN, 
      children: [
      { 
        path: ':insurancePlanId/:cropYear/:policyId/:declaredYieldContractGuid', component: GrainDopContainer, data: {scopes: [ROUTE_SCOPES.DOP]},
        canActivate: [ResourcesAuthGuard],
        canDeactivate: [DeactivateGuard]
      },
      ] 
    },

    { path: R.DOP_FORAGE, 
      children: [
      { 
        path: ':insurancePlanId/:cropYear/:policyId/:declaredYieldContractGuid', component: ForageDopContainer, data: {scopes: [ROUTE_SCOPES.DOP]},
        canActivate: [ResourcesAuthGuard],
        canDeactivate: [DeactivateGuard]
      },
      ] 
    },

    { path: R.DOP_BERRIES, 
      children: [
      { 
        path: ':insurancePlanId/:cropYear/:policyId/:declaredYieldContractGuid', component: ForageDopContainer, data: {scopes: [ROUTE_SCOPES.DOP]},
        canActivate: [ResourcesAuthGuard],
        canDeactivate: [DeactivateGuard]
      },
      ] 
    },

    
    // lazy loading chunks
    // grain verified yield
    { 
      path: R.VERIFIED_YIELD, 
      children: [{
          path: '',
          loadChildren: () => import('src/app/routes/verified-yield-routing.module')
            .then(m => m.VerifiedYieldRoutingModule)
        }] 
    },
    // land management
    { 
      path: R.LAND, 
      children: [{
          path: '',
          loadChildren: () => import('src/app/routes/land-routing.module')
            .then(m => m.LandRoutingModule)
        }] 
    },
    // support data
    { 
      path: R.MAINTAIN, 
      children: [{
          path: '',
          loadChildren: () => import('src/app/routes/maintain-routing.module')
            .then(m => m.MaintainRoutingModule)
        }] 
    },
    // end of lazy load chunks

    { path: R.LANDING_PAGE, 
      children: [
      { 
        path: ':policyNumber/:targetScreen', component: LandingPageComponent, data: {scopes: [ROUTE_SCOPES.LANDING]},
        canActivate: [ResourcesAuthGuard]
      },
      ] 
    },
  
    { path: R.USER_SETTINGS, component: UserSettingsContainer, data: {scopes: [ROUTE_SCOPES.USER_SETTINGS]},  pathMatch: "full", canActivate: [ResourcesAuthGuard], canDeactivate: [DeactivateGuard]  },
    
    { path: R.UNAUTHORIZED, component: UnauthorizedPageComponent },
    { path: R.SIGN_OUT, component: SignOutPageComponent, pathMatch: "full" },
    { path: "**", redirectTo: R.LANDING }
];

@NgModule({
    imports: [RouterModule.forRoot(routesDesktop, {})],
    exports: [RouterModule]
})
export class AppRoutingModule {
    public constructor(router: Router,
                       applicationStateService: ApplicationStateService) {
    }
}
