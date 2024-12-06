package net.mirolls.thecoins.libs;

import java.util.Random;

public class RandomCharacterPicker {
  public static String getRandomCharacter() {
    String[] characters = {
        "Albedo", "Alhaitham", "Kamisato Ayaka", "Eula", "Ganyu", "Hu Tao", "Kaedehara Kazuha", "Keqing", "Sigewinne",
        "Klee", "Kokomi", "Mona", "Nahida", "Neuvillette", "Raiden Shogun", "Shenhe", "Venti", "Xiao", "Zhongli",
        "Mahiro Oyama", "Miharu Oyama", "Maple", "Momiji Hozuki", "Miya Murosaki",
        "Bronya", "Gepard", "Kafka", "March 7th", "Seele", "Silver Wolf",
        "Yanqing", "Jing Yuan", "Luocha", "Clara", "Blade", "Topaz"
    };

    Random random = new Random();
    return characters[random.nextInt(characters.length)];
  }
}