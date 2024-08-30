export interface Ingredient{
  ingredient_id: number;
  name: string;
  description?: string;
  type?: string;
  isAlcohol?: boolean;
  strength?: string;
  imageURL?: string;
  amount?: string;
}
