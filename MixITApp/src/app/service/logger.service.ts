import { Injectable } from '@angular/core';
import {GlobalConfiguration} from "../config/GlobalConfiguration";

@Injectable({
  providedIn: 'root'
})
export class LoggerService {

  log(message: string, data?: any): void {
    if (this.isDevelopment()) {
      console.log(`[INFO] ${message}`, data || '');
    }
  }

  logError(message: string, error: any): void {
    if (this.isDevelopment()) {
      console.error(`[ERROR] ${message}`, error);
    }
  }

  logWarning(message: string, warning: any): void {
    if (this.isDevelopment()) {
      console.warn(`[WARNING] ${message}`, warning);
    }
  }

  private isDevelopment(): boolean {
    return GlobalConfiguration.DEV_MODE;
  }
}
