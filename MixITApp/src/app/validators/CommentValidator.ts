import {bannedWords} from "./bannedWords";

export class CommentValidator{

  static isValid(control: string): boolean {
    if (control.length <= 0 || control.length > 1000) return false;

    const lowerCaseControl = control.toLowerCase();
    const hasBannedWord = bannedWords.some(word => lowerCaseControl.includes(word.toLowerCase()));

    return !hasBannedWord;
  }

}
