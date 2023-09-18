import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Loader } from '@googlemaps/js-api-loader';
import { Marker } from '@syncfusion/ej2-maps';
import { ConfigService } from 'src/app/config/config.service';
import { Employee } from 'src/app/employee';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-map-component',
  templateUrl: './map-component.component.html',
  styleUrls: ['./map-component.component.css']
})
export class MapComponentComponent implements OnInit,Resolve<any>{

  title="google-maps";
  public hotels:Employee[] | undefined;
  public map:google.maps.Map | undefined;
  public markers:google.maps.Marker[];
  public locations:any[];
  

  

   constructor(private configService:ConfigService,
    private route: Router,
    private activateRoute: ActivatedRoute,
    ){
    this.hotels=this.activateRoute.snapshot.data['data'];
  }
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    throw new Error('Method not implemented.');
  }
  

  

  public getEmployees(): void {
  
    this.configService.getEmployees().subscribe(
      
      (response: Employee[]) => {
        this.hotels = response;
        console.log(this.hotels);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
      
    );
  }

  
   ngOnInit():void {
    this.showMap();

  }

  loadAllMarkers(){
    this.markers.forEach(markerInfo => {
      console.log
    })
  }

  showMap(){
    
    let loader = new Loader({
      apiKey:'AIzaSyCBQHpSfY2NfXhJy7rHqn-bmQN_GGSFOGI',
    });

    loader.load().then(()=>{
      console.log("Loaded map")

      const location = {
        lat:46.8182,
        lng:8.2275,
      }
      
      this.map=new google.maps.Map(document.getElementById('map')!,{
        center:location,
        zoom:8,
      })

      

      // const marker= new google.maps.Marker({
      //   position:location,
      //   map:this.map
      // })



      debugger
      const svgMarker = {
        path: "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
        fillColor: "blue",
        fillOpacity: 0.8,
        strokeWeight: 0,
        rotation: 0,
        scale: 2,
        anchor: new google.maps.Point(15, 30),
      };

      const image = {
        url: "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
        // This marker is 20 pixels wide by 32 pixels high.
        size: new google.maps.Size(20, 32),
        // The origin for this image is (0, 0).
        origin: new google.maps.Point(0, 0),
        // The anchor for this image is the base of the flagpole at (0, 32).
        anchor: new google.maps.Point(0, 32),
      };

      const shape = {
        coords: [1, 1, 1, 20, 18, 20, 18, 1],
        type: "poly",
      };
      
      for(let hotel of Object.values(this.hotels)){
        const location1={
          lat:hotel.y,
          lng:hotel.x
        }
        debugger
          const marker = new google.maps.Marker({
            position:location1,
            map:this.map,
            title:hotel.city,
            icon:image,
            shape:shape,
            optimized:false,
            clickable:true,
          })
          debugger
          
      }
     

    })

  }
  
}
