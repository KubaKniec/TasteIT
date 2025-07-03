import { Component } from '@angular/core';
interface Developer {
  name: string;
  role: string;
  description: string;
  imageUrl?: string;
}
@Component({
  selector: 'app-app-info',
  templateUrl: './app-info.component.html',
  styleUrls: ['./app-info.component.css']
})
export class AppInfoComponent {
  developers: Developer[] = [
    {
      name: 'Jakub Konkol',
      role: 'Project Manager, Full Stack Developer',
      description: 'Developed the entire frontend and contributed to backend development.'
    },
    {
      name: 'Mikołaj Kawczyński',
      role: 'Backend Developer, QA, Algorithm Developer',
      description: 'Developed the backend and the recommendation algorithm. Responsible for quality assurance.'
    },
    {
      name: 'Filip Kliczewski',
      role: 'Backend Developer, Tester',
      description: 'Implemented backend features, tested the application, and kept the team motivated.'
    }
  ];

}
