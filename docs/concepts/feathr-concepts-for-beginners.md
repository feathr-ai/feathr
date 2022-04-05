# Feathr Concepts for Beginners

Talk about Feathr data model
- Observation data, why doingg joins, etc.

Additional datathat have additional features, which you want to augment the centralized dataset. For example, user purchase history, total spending in the past month.


Deep learning scenario? 

Talk about feathr object model

- projects, anchors, relationships, feature tables, etc.

```mermaid
classDiagram
    Animal <|-- Duck
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : +int age
    Animal : +String gender
    Animal: +isMammal()
    Animal: +mate()
    class Duck{
      +String beakColor
      +swim()
      +quack()
    }
    class Fish{
      -int sizeInFeet
      -canEat()
    }
    class Zebra{
      +bool is_wild
      +run()
    }
```