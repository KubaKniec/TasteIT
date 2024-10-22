import {Injectable} from "@angular/core";
import {AngularFireStorage} from "@angular/fire/compat/storage";
import {finalize, Observable} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class StorageUploadService{
  constructor(private storage: AngularFireStorage) {}
  /**
   * Zwraca Observable z procentowym postępem przesyłania pliku.
   * @param file Plik do przesłania
   * @param filePath Ścieżka, gdzie plik zostanie zapisany
   * @returns Observable z procentowym postępem
   */
  uploadFile(file: File, filePath: string): Observable<string>{
    const fileRef = this.storage.ref(filePath);
    const uploadTask = this.storage.upload(filePath, file);

    return new Observable((observer) => {
      uploadTask.snapshotChanges().pipe(
        finalize(() => {
          fileRef.getDownloadURL().subscribe((url) => {
            observer.next(url);
            observer.complete();
          });
        })
      ).subscribe();
    });
  }
  /**
   * Usuwa plik z Firebase Storage.
   * @param fileUrl URL pliku do usunięcia
   * @returns Observable po zakończeniu usuwania
   */
  deleteFile(fileUrl: string): Observable<void>{
    return new Observable((observer) => {
      this.storage.ref(fileUrl).delete().subscribe(() => {
        observer.next();
        observer.complete();
      });
    })
  }
}
