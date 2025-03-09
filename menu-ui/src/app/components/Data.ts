import ChickenBurger from '@/../public/chickenBurger.svg';
import ChickenPizza from '@/../public/ChickenPizza.svg';
import GrillChicken from '@/../public/GrillChicken.svg';
import Platter from '@/../public/FoodPlater.svg';
import Burger from '@/../public/chickenBurger.svg';
import FrenchFry from '@/../public/FrenchFry.svg';
import CocaCola from '@/../public/CocaCola.svg';
import Toast from '@/../public/Toast.svg';

export interface ItemType {
    id: number;
    name: string;
    tag: string;
    price: string;
    image: any;
    mainImage: any;
    description: string;
}
export const MenuItem = () : ItemType[] => {
    return [
        {
            id: 1,
            name: 'Chicken Burger',
            tag: 'Home',
            price: '$13.50',
            image: ChickenBurger,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce.',
        },
        {
            id: 2,
            name: 'Chicken Pizza',
            tag: '',
            price: '$15.50',
            image: ChickenPizza,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',
        },
        {
            id: 3,
            name: 'Grill Chicken',
            tag: 'Home',
            price: '$8.50',
            image: GrillChicken,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',
        },
        {
            id: 4,
            name: 'Food Plater',
            tag: 'Special',
            price: '$10.00',
            image: Platter,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',

        },
        {
            id: 5,
            name: 'Food Plater',
            tag: 'Special',
            price: '$20.00',
            image: Platter,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',
        }
    ]
} 

export const AddOns = () => {
    return [
        {
            id: 1,
            name: 'French Fries',
            tag: 'Home',
            price: '$5.0',
            image: FrenchFry,
            mainImage: FrenchFry,
            description: 'Crispy Fries',
        },
        {
            id: 2,
            name: 'Coca Cola',
            tag: '',
            price: '$10.0',
            image: CocaCola,
            mainImage: CocaCola,
            description: 'Coca Cola',
        },
        {
            id: 3,
            name: 'Toast',
            tag: 'Home',
            price: '3.00',
            image: Toast,
            mainImage: Toast,
            description: 'Crispy Toast',
        }
    ]
}