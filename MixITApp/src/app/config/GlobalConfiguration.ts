
export const GlobalConfiguration = {
  APP_VERSION: '1.0.0',
  APP_NAME: 'TasteIT',
  /**
   * Determines if the app should allow all devices like PC, Mobile, Tablet, etc.
   * If false, the app will only allow mobile devices in standalone mode (PWA) or native app.
   */
  ALLOW_ALL_DEVICES: true,
  SHOW_WARNINGS: false,
  /**
   * Determines if the app is in development mode.
   * If true, the app will use more verbose logging
   */
  DEV_MODE: false,
  /**
   * If true, the recommendation algorithm will be used to suggest posts to users.
   * If false, basic feed will be used.
   */
  USE_RECOMMENDATION_ALGORITHM: true,

}
