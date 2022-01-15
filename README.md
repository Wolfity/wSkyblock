# wSkyblock
This is a skyblock plugin, including configurable skills, 
shops, warps, auction house, magic market, mob arenas, custom crafting recipes ,
and more!


### **How to set it up**
**Hub**\
First of all, you want to create a hub spawn point; 
you can do that by simply using the [/sethub] command.


## **Features** 
**Congratulations**\
That was all the setting up that needed to happen. 
Below I will go over all features and how you can use them.
You can go to your island using the [/is] command. 
Or by opening the skyblock profile menu using the [/sb] command.

### **Skills**
There are three types of skills, lumberjack, monster killer, 
and miner. What do these skills do? Each of these skills has its benefit.

**Monster Killer**
You can level up the monster killer skill by killing mobs; 
you can configure which mobs give how much XP, the amount of XP required for the next level, 
the max level!

**Miner**\
You can level up the Miner skill by mining ores; 
you can configure which ores give how much XP, 
the amount of XP required for the next level, the max level, 
the chances of getting lucky!

**Lumberjack**\
You can level up the Lumberjack skill by chopping wood; 
you can configure which wood gives how much XP, the amount of XP 
required for the next level, the max level, the chances of getting lucky!


### **Auction House**
The auction house is a space where players can sell their items to other players.
These items are saved in a database and loaded up on the server's start. 
You can always cancel your auctions by going to the auction manager menu, 
where you can click your auction and cancel it. You will then get your item back!

The auction House also has the option to filter items. The currently supported filters
are, filtering from expensive to cheap, from cheap to expensive, and by text.

### **Shops**
Default shops are also provided! There are currently six shops.
Block, Food, Misc, Mob Drops, Tools, and Valuable shops. 
Every item and the price of it in these shops are configurable. 
Not only that, but you can also configure the shop icons in the shop menu and choose which slot
they appear on. There is also a feature that allows you to choose exactly
how much of an item you want to buy.

You can not only buy items in the shop,
but you can also sell them. Buy and Sell prices are entirely configurable.
If you want a shop to be temporarily disabled, you can.
All you have to do is head over to that shop’s config file and change “disabled” 
from true to false.

### **Magic Market**
The Magic Market is where you can buy items with custom enchants. 
These items are entirely configurable, so are their prices. 
You can modify the custom enchantments on the items in the customenchants.yml file.
Examples of custom enchants are speed boots. You gain an extra speed level for every 
level while wearing the items with that enchantment.
Another example is Telekinesis. This enchant ensures you don’t have to worry about 
other players picking up the ores you mine! They will directly appear in your inventory.

### **Warps**
Got a massive island, and are you tired of having to remember coordinates and having 
to walk over to the specific spot every time? No worries, that’s where warps come in handy!
Go to a location on your island and type [/is addwarp <name>]. There are two ways of using 
the warp, you can either type [/warp <name>], or you can open the warp GUI by typing [/wars],
and then click the warp you want to go to.

Don’t need a warp anymore? No worries, just execute the [/is removewarp <name>],
and the warp will be deleted!

### **Mob Arenas**
Do you have strong gear and weapons? Want to level up your monster killer skill faster?
Mob Arena’s are here. 
What exactly is a mob arena? A mob arena is a location where stronger custom mobs spawn.
These mobs are configurable. You can configure how much XP they give, how much health they have,
the damage they do, and how fast they are!

Another cool thing you can do is configure how many mobs there can maximally 
be in an arena and how often mobs spawn!

**How To Set Them Up?**

Creating mob arena’s is pretty simple. 
First, you will have to use the command [mobarena create <name>]. 
After that, the plugin will send you a message informing you that the creation was successful.
After that, you will have to set the bounds of the arena. 
Simply choose a point on the ground and execute the command [/mobarena addbound <nam>]; 
after, choose a second point (preferably somewhere in the air).
And run the same command.
The plugin will let you know that the arena cuboid was successfully 
created once these steps are completed.

The last thing to do is dive into the mobarena.yml file and edit any 
values you want to change. Make sure to restart your server once all the arenas are created.

You can delete mob arenas by using the command [/mobarena remove <name>]

### **Custom Crafting Recipes**
Currently, there are four custom crafting recipes. 
There is a Timber Axe recipe, which will create a Golden Axe with the Timber custom enchant.
This enchants mines several connected blocks of wood at the same time.
This significantly speeds up the process of leveling up the Lumberjack skill!
You can craft it this way.

(this is how it would look in the crafting table!)
Ancient Debris | Diamond      | Ancient Debris
Ancient Debris | Golden Axe  | Ancient Debris
Ancient Debris | Diamond      | Ancient Debris

Then we have a Telekinesis Pickaxe. 
This pickaxe ensures that you don’t have to pick any blocks up. 
They will automatically appear in your inventory. It can be crafted this way.

(this is how it would look in the crafting table!)\
``Ancient Debris | Diamond        | Ancient Debris``\
``Ancient Debris | Iron Pickaxe   | Ancient Debris``\
``Ancient Debris | Diamond        | Ancient Debris``

The next item is an Enchanted Golden Apple. 
Who doesn’t love an OP healing item? It can be crafted using these items in these places.

(this is how it would look in the crafting table!)\
``Gold Block| Gold Block     | Gold Block``\
``Gold Block| Golden Apple   | Gold Block``\
``Gold Block| Gold Block     | Gold Block``

The last custom crafting recipe is a god potion. 
An extremely overpowered potion with several effects which last up to 30 minutes. 
You can craft it this way.

(this is how it would look in the crafting table!)\
``Gold Block | Diamond Block | Gold Block``\
``Gold Block | Water Bucket    | Gold Block``\
``Gold Block | Diamond Block | Gold Block``


### **Commands**
``/sb`` - Open the skyblock profile menu.\
``/is`` - Teleports you to your own island.\
``/magicmarket`` - Opens the Magic Market shop.\
``/ah`` - Opens the Auction House.\
``/warp <place> `` - Warps you to the specified warp.\
``/warp`` - Opens the Warp Menu.\
``/shop`` - Opens the Shop Menu.\
``/skills`` - Open the Skills Menu.\
``/hub`` - Teleports you to the hub.\

### **Admin Commands** 
(requires the permission sb.admin)\
``/sethub`` - Sets the Hub Location.\
``/givecoins <player> <amount>`` - Gives the specified player an amount of coins.\
``/removecoins <player> <amount>`` - Removes the specified amount of coins from the player.\
``/mobarena <create> <name>`` - Create a mob arena\
``/mobarena <addbound> <name>`` - Add a bound to the created mob arena.
### **Permissions**
``sb.admin`` --> Admin Permissions







