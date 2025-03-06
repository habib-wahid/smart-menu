import ChickenBurger from '@/../public/chickenBurger.svg';
import ChickenPizza from '@/../public/ChickenPizza.svg';
import GrillChicken from '@/../public/GrillChicken.svg';
import Platter from '@/../public/FoodPlater.svg';
import Burger from '@/../public/chickenBurger.svg';

export const MenuItem = () => {
    return [
        {
            id: 1,
            name: 'Chicken Burger',
            tag: 'Home',
            price: '$299',
            image: ChickenBurger,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce.',
        },
        {
            id: 2,
            name: 'Chicken Pizza',
            tag: '',
            price: '$199',
            image: ChickenPizza,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',
        },
        {
            id: 3,
            name: 'Grill Chicken',
            tag: 'Home',
            price: '$299',
            image: GrillChicken,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',
        },
        {
            id: 4,
            name: 'Food Plater',
            tag: 'Special',
            price: '$299',
            image: Platter,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',

        },
        {
            id: 5,
            name: 'Food Plater',
            tag: 'Special',
            price: '$299',
            image: Platter,
            mainImage: Burger,
            description: 'Big juicy chicken burger with cheese, lettuce, tomato, onions and special sauce',
        }
    ]
}