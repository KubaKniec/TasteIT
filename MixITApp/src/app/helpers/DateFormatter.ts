export class DateFormatter{
  static getFormattedDate(dateInput: Date): string {
    let date = new Date(dateInput);
    let now = new Date();
    let diffInMilliseconds = now.getTime() - date.getTime();
    let diffInHours = Math.floor(diffInMilliseconds / (1000 * 60 * 60));
    let diffInDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24));

    if (diffInHours < 1) {
      return 'Less than an hour ago';
    } else if (diffInHours < 24) {
      return `${diffInHours} hours ago`;
    } else if (diffInDays <= 7) {
      return `${diffInDays} days ago`;
    } else {
      return date.toLocaleDateString('en-GB');
    }
  }

}
