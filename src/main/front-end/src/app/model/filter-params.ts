import {Gender} from "./enum/gender";;
import {PageUser} from "./page-user";

export class FilterParams{
  private name: string;
  private gender: Gender;
  private ageMin: number;
  private ageMax: number;
  private city: string;
  private ratingMin: number;
  private ratingMax: number;
  private sort: string;
  private page: number;

  public setName(name: string){
    this.name = name;
  }

  public setGender(gender: Gender){
    this.gender = gender;
  }

  public setAgeMin(ageMin: number){
    this.ageMin = ageMin;
  }

  public setAgeMax(ageMax: number){
    this.ageMax = ageMax;
  }

  public setCity(city: string){
    this.city = city;
  }

  public setRatingMin(ratingMin: number){
    this.ratingMin = ratingMin;
  }

  public setRatingMax(ratingMax: number){
    this.ratingMax = ratingMax;
  }

  public setSort(sort: string){
    this.sort = sort;
  }

  public setPage(page: number){
    this.page = page;
  }
}

