import {Injectable} from '@angular/core';
import {Camera, CameraResultType, CameraSource} from '@capacitor/camera';
import {Platform} from '@ionic/angular';

@Injectable({
  providedIn: 'root',
})
export class CameraService {

  constructor(private platform: Platform) {}

  public async takePhoto(){
    return await Camera.getPhoto({
      quality: 100,
      allowEditing: true,
      resultType: CameraResultType.Uri,
      source: CameraSource.Camera,
    });
  }


}

export interface UserPhoto {
  filepath: string;
  webviewPath: string;
}
