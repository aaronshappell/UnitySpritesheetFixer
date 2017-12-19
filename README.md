# UnitySpritesheetFixer
Currently a commandline tool to convert 2D sprite sheets to fixed versions to prevent pixel gaps in Unity.

## Usage
-i/--input <file> -tw/--tileWidth <width> optional: -o/--output <file> -th/--tileHeight <height>

The input file and tile width must be specified (-i and -tw). The output flag is optional (defaults to <file>Fixed.<format>) and tile height flag is optional (defaults to tile width).

`java -jar UnitySpritesheetFixer -i file.png -tw 32`