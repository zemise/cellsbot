# CellsBot

CellsBot is an MC-QQ bot plugin for Minecraft servers.
CellsBot-alone also can run in docker without Minecraft.

## Building
CellsBot uses Gradle to handle dependencies & building.

#### Requirements
* Java 17 JDK or newer
* Git
* Gradle 7.4

#### Compiling from source
```bash
git clone https://github.com/zemise/cellsbot
cd CellsBot/
gradle clean common:build
gradle clean alone:build
gradle bungee:build
gradle bukkit:build
gradle waterfall:build
```
You can find the output jars in the `loader/build/libs` or `build/libs` directories.

You also can build module cellsbot:alone by docker
```bash
git clone https://github.com/zemise/cellsbot
cd CellsBot/
gradlew clean common:build
gradlew alone:docker
```


## Contributing
#### Pull Requests
If you make any changes or improvements to the plugin which you think would be beneficial to others, please consider making a pull request to merge your changes back into the upstream project. (especially if your changes are bug fixes!)

CellsBot loosely follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Generally, try to copy the style of code found in the class you're editing.

#### Project Layout
The project is split up into a few separate modules.
* 
* **Common** - The common module contains most of the code which implements the respective CellsBot plugins. This abstract module reduces duplicated code throughout the project.
* **Bukkit, BungeeCord** - Each use the common module to implement plugins on the respective server platforms.

## License
CellsBot is licensed under the permissive MIT license. Please see [`LICENSE.txt`](https://github.com/abc408880155/cellsbot/blob/main/LICENSE.txt) for more info.

