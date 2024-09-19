export class CommentValidator{

  static validate(control: string): boolean {
     // TODO: Implement more complex validation
    return (control.length < 0 || control.length > 1000);

  }

}
