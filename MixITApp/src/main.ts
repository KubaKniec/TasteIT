import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';
import { defineCustomElements } from '@ionic/pwa-elements/loader';

defineCustomElements(window).catch(err => {
  console.error(err);
});

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
