import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ResourcesAuthGuard } from '../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../services/util/DeactivateGuard';
import { ROUTE_SCOPES } from '../utils/scopes';
import { DashboardContainer } from '../containers/maintenance/dashboard-container.component';
import { SeedingDeadlinesContainer } from '../containers/maintenance/seeding-deadlines-container.component';
import { ResourcesRoutes } from '../utils/constants';
import { GradeModifiersContainer } from '../containers/maintenance/grade-modifiers-container.component';
import { ForageVarietyInsurabilityContainer } from '../containers/maintenance/forage-variety-insurability-container.component';
import { YieldConversionContainer } from '../containers/maintenance/yield-conversion-container.component';


const routes: Routes = [
    { 
        path: ResourcesRoutes.MAINTAIN_DASHBOARD.split('/')[1], // -> 'dashboard', 
        component: DashboardContainer, 
        data: {scopes: [ROUTE_SCOPES.MAINTAIN]}, 
        pathMatch: "full"
    },
    { 
        path: ResourcesRoutes.MAINTAIN_SEEDING_DEADLINES.split('/')[1],  
        component: SeedingDeadlinesContainer, 
        data: {scopes: [ROUTE_SCOPES.MAINTAIN_SEEDING_DEADLINES]}, 
        pathMatch: "full", 
        canActivate: [ResourcesAuthGuard], 
        canDeactivate: [DeactivateGuard] 
    },
    { 
        path: ResourcesRoutes.MAINTAIN_GRADE_MODIFIERS.split('/')[1],
        component: GradeModifiersContainer, 
        data: {scopes: [ROUTE_SCOPES.MAINTAIN_GRADE_MODIFIERS]}, 
        pathMatch: "full", 
        canActivate: [ResourcesAuthGuard], 
        canDeactivate: [DeactivateGuard] 
    },
    { 
        path: ResourcesRoutes.MAINTAIN_FORAGE_VARIETY_INSURABILITY.split('/')[1],
        component: ForageVarietyInsurabilityContainer, 
        data: {scopes: [ROUTE_SCOPES.MAINTAIN_FORAGE_VARIETY_INSURABILITY]},  
        pathMatch: "full", 
        canActivate: [ResourcesAuthGuard], 
        canDeactivate: [DeactivateGuard] 
    },
    { 
        path: ResourcesRoutes.MAINTAIN_YIELD_CONVERSION.split('/')[1],
        component: YieldConversionContainer, 
        data: {scopes: [ROUTE_SCOPES.MAINTAIN_YIELD_CONVERSION]},  
        pathMatch: "full", 
        canActivate: [ResourcesAuthGuard], 
        canDeactivate: [DeactivateGuard] 
    },
];

@NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      DashboardContainer,
      SeedingDeadlinesContainer,
      GradeModifiersContainer,
      ForageVarietyInsurabilityContainer,
      YieldConversionContainer
    ]
})

export class MaintainRoutingModule {}