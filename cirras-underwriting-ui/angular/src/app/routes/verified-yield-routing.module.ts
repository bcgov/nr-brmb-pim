import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ResourcesAuthGuard } from '../services/util/ResourcesAuthGuard';
import { DeactivateGuard } from '../services/util/DeactivateGuard';
import { ROUTE_SCOPES } from '../utils/scopes';
import { VerifiedYieldContainer } from '../containers/verified-yield/verified-yield-container.component';

const routes: Routes = [
    {
        path: ':insurancePlanId/:cropYear/:policyId/:verifiedYieldContractGuid', 
        component: VerifiedYieldContainer, 
        data: {scopes: [ROUTE_SCOPES.VERIFIED_YEILD]},
        canActivate: [ResourcesAuthGuard],
        canDeactivate: [DeactivateGuard]
    }
];

@NgModule({
    imports: [
      CommonModule,
      RouterModule.forChild(routes),
      VerifiedYieldContainer
    ]
})

export class VerifiedYieldRoutingModule {}