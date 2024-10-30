import { NgModule } from '@angular/core';
import { GestureCloseDirective } from './directives/GestureCloseDirective';

@NgModule({
  declarations: [GestureCloseDirective],
  exports: [GestureCloseDirective]
})
export class GesturesModule { }
