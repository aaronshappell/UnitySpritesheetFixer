/**
 * UnitySpritesheetFixer is a commandline utility to fix spritesheets in unity to prevent gaps between tiles.
 * It is run with "java -jar UnitySpritesheetFixer -i file.png -o file.png -tw 32 -th 32"
 * The output flag <-o> and tile height flag <-th> are optional.
 * Needs at least "java -jar UnitySpritesheetFixer -i file.png -tw 32" to run
 * @author Aaron Shappell
 */

package io.github.aaronshappell.spritesheetFixer

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.system.exitProcess

fun main(args: Array<String>){
    var inputFile: String = ""
    var outputFile: String = ""
    var tileWidth: Int = -1
    var tileHeight: Int = -1
    if(args.isEmpty()) {
        println("Please specify an input file and tile width, see -h or --help for details")
        exitProcess(-1)
    } else if(args.size == 1 && args[0] == "-h" || args[0] == "--help"){
        println("java -jar UnitySpritesheetFixer -i/--input <file> -tw/-tileWidth <width> (optional): -o/--output <file> -th/--tileHeight <height>")
        return
    }
    for(i in args.indices step 2){
        when(args[i]){
            "-i", "--input" -> inputFile = args[i + 1]
            "-o", "--output" -> outputFile = args[i + 1]
            "-tw", "--tileWidth" -> tileWidth = try{Integer.parseInt(args[i + 1])}catch(e: NumberFormatException){tileWidth}
            "-th", "--tileHeight" -> tileHeight = try{Integer.parseInt(args[i + 1])}catch(e: NumberFormatException){tileHeight}
        }
    }
    if(inputFile == ""){
        println("Please specify an input file")
        exitProcess(-1)
    } else if(inputFile.substring(inputFile.indexOf(".") + 1) !in ImageIO.getReaderFormatNames()){
        println("Error: \"${inputFile.substring(inputFile.indexOf(".") + 1)}\" is not a supported input file format")
        exitProcess(-1)
    }
    if(tileWidth == -1){
        println("Please specify a tile width")
        exitProcess(-1)
    } else if(tileWidth < 0){
        println("Error: tile width cannot be negative")
        exitProcess(-1)
    }
    if(outputFile == ""){
        outputFile = inputFile.substring(0, inputFile.indexOf(".")) + "Fixed" + inputFile.substring(inputFile.indexOf("."))
    } else if(outputFile.substring(outputFile.indexOf(".") + 1) !in ImageIO.getWriterFormatNames()){
        println("Error: \"${outputFile.substring(outputFile.indexOf(".") + 1)}\" is not a supported output file format")
        exitProcess(-1)
    }
    if(tileHeight == -1){
        tileHeight = tileWidth
    } else if(tileHeight < 0){
        println("Error: tile height cannot be negative")
        exitProcess(-1)
    }
    var input: BufferedImage
    try{
        input = ImageIO.read(File(inputFile))
    } catch(e: IOException){
        println("Error: could not load file: $inputFile")
        exitProcess(-1)
    }
    val output: BufferedImage = BufferedImage(input.width + input.width / tileWidth * 2, input.height + input.height / tileHeight * 2, input.type)
    for(x in 0 until input.width step tileWidth){
        for(y in 0 until input.height step tileHeight){
            output.setRGB(x + 1 + 2 * x / tileWidth, y + 1 + 2 * y / tileHeight, tileWidth, tileHeight, input.getRGB(x, y, tileWidth, tileHeight, null, 0, tileWidth), 0, tileWidth)
        }
    }
    for(x in 0 until output.width step tileWidth + 2){
        output.setRGB(x, 0, 1, output.height, output.getRGB(x + 1, 0, 1, output.height, null, 0, 1), 0, 1)
        output.setRGB(x + tileWidth + 1, 0, 1, output.height, output.getRGB(x + tileWidth, 0, 1, output.height, null, 0, 1), 0, 1)
    }
    for(y in 0 until output.height step tileHeight + 2){
        output.setRGB(0, y, output.width, 1, output.getRGB(0, y + 1, output.width, 1, null, 0, output.width), 0, output.width)
        output.setRGB(0, y + tileHeight + 1, output.width, 1, output.getRGB(0, y + tileHeight, output.width, 1, null, 0, output.width), 0, output.width)
    }
    try{
        ImageIO.write(output, outputFile.substring(outputFile.indexOf(".") + 1), File(outputFile))
    } catch(e: IOException){
        println("Error: could not write file")
        exitProcess(-1)
    }
}