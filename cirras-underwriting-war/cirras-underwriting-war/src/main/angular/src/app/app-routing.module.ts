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
import { LandListContainer } from "./containers/land-management/land-list-container.component";
import { ManageLandComponentContainer } from "./containers/land-management/manage-land-container.component";
import { LandingPageComponent } from "./components/common/landingpage/landingpage.component";
import { SeedingDeadlinesContainer } from "./containers/maintenance/seeding-deadlines-container.component";
import { GradeModifiersContainer } from "./containers/maintenance/grade-modifiers-container.component";
import { DashboardContainer } from "./containers/maintenance/dashboard-container.component";
import { ForageVarietyInsurabilityContainer } from "./containers/maintenance/forage-variety-insurability-container.component";
import { YieldConversionContainer } from "./containers/maintenance/yield-conversion-container.component";
import { DeactivateGuard } from "./services/util/DeactivateGuard";
import { ForageDopContainer } from "./containers/dop/forage-dop-container.component";
import { VerifiedYieldContainer } from "./containers/verified-yield/verified-yield-container.component";
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

    // grain verified yield
    { path: R.VERIFIED_YIELD, 
      children: [
      { 
        path: ':insurancePlanId/:cropYear/:policyId/:verifiedYieldContractGuid', component: VerifiedYieldContainer, data: {scopes: [ROUTE_SCOPES.VERIFIED_YEILD]},
        canActivate: [ResourcesAuthGuard],
        canDeactivate: [DeactivateGuard]
      },
      ] 
    },

    { path: R.LAND_LIST, component: LandListContainer, data: {scopes: [ROUTE_SCOPES.LEGAL_LAND]},  pathMatch: "full" },
    { path: R.LAND_MANAGE, 
      children: [
        { 
          path: ':legalLandId', component: ManageLandComponentContainer, data: {scopes: [ROUTE_SCOPES.LEGAL_LAND]},
          canActivate: [ResourcesAuthGuard],
          canDeactivate: [DeactivateGuard]
        },
        ] 
    },
    { path: R.LANDING_PAGE, 
      children: [
      { 
        path: ':policyNumber/:targetScreen', component: LandingPageComponent, data: {scopes: [ROUTE_SCOPES.LANDING]},
        canActivate: [ResourcesAuthGuard]
      },
      ] 
    },

    { path: R.MAINTAIN_DASHBOARD, component: DashboardContainer, data: {scopes: [ROUTE_SCOPES.MAINTAIN]}, pathMatch: "full"},
    { path: R.MAINTAIN_SEEDING_DEADLINES, component: SeedingDeadlinesContainer, data: {scopes: [ROUTE_SCOPES.MAINTAIN_SEEDING_DEADLINES]}, pathMatch: "full", canActivate: [ResourcesAuthGuard], canDeactivate: [DeactivateGuard] },
    { path: R.MAINTAIN_GRADE_MODIFIERS, component: GradeModifiersContainer, data: {scopes: [ROUTE_SCOPES.MAINTAIN_GRADE_MODIFIERS]}, pathMatch: "full", canActivate: [ResourcesAuthGuard], canDeactivate: [DeactivateGuard] },
    { path: R.MAINTAIN_FORAGE_VARIETY_INSURABILITY, component: ForageVarietyInsurabilityContainer, data: {scopes: [ROUTE_SCOPES.MAINTAIN_FORAGE_VARIETY_INSURABILITY]},  pathMatch: "full", canActivate: [ResourcesAuthGuard], canDeactivate: [DeactivateGuard] },
    { path: R.MAINTAIN_YIELD_CONVERSION, component: YieldConversionContainer, data: {scopes: [ROUTE_SCOPES.MAINTAIN_YIELD_CONVERSION]},  pathMatch: "full", canActivate: [ResourcesAuthGuard], canDeactivate: [DeactivateGuard] },
  
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
        // if (applicationStateService.getIsMobileResolution()) {
        //     console.log("mobile mode");
        //     router.resetConfig(routesMobile);
        // }
    }
}
