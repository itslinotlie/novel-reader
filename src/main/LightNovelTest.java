package main;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;

public class LightNovelTest extends JFrame {
    static String information = "";
    static String summary = "";
    static String description = "";
    static int WIDTH=600, HEIGHT = 800;
    static int maxChap = 0, minChap = 0x3f3f3f3f;
    public LightNovelTest() {
        setupLabel();
        setupFrame();
    }
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setLayout(new BorderLayout());
    }
    private void setupLabel() {

        JPanel bot = new JPanel();
        bot.setBackground(Color.RED);
        bot.setPreferredSize(new Dimension(WIDTH, 100));
        add(bot, BorderLayout.SOUTH);

        JPanel top = new JPanel();
        top.setBackground(Color.BLACK);
        top.setPreferredSize(new Dimension(WIDTH, 100));
        add(top, BorderLayout.NORTH);

        JPanel left = new JPanel();
        left.setBackground(Color.BLUE);
        left.setPreferredSize(new Dimension(10, HEIGHT));
        add(left, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setBackground(Color.cyan);
        right.setPreferredSize(new Dimension(10, HEIGHT));
        add(right, BorderLayout.EAST);


//        JTextArea text = new JTextArea(information);
        JTextArea text = new JTextArea(x);
        text.setBackground(Color.GREEN);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
//        text.setOpaque(false);
        add(text);

        JScrollPane scroll = new JScrollPane(text);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        add(scroll);
    }

    public static void main(String[] args) {
//        String website = "https://novelfull.com";
//        String title[] = {"/overgeared", "/the-kings-avatar"};
//        String novel = title[0];
//        String chapter = "/chapter-1019";
//        String thumbnail = "";
//        String html = "";
//
//        String url = website+novel+".html";
//        try { //novel information
//            Document doc = Jsoup.connect(url).get();
//            html = doc.outerHtml();
//            thumbnail = doc.select("div.book").first().select("img").attr("src");
//                    //doc.select("book").select("img").attr("src"));
//
//
//            summary+=doc.select(".desc-text").text(); //novel summary
//            description+=doc.select(".info").text(); //author, genre, source, status
//            for(Element row:doc.getElementsByTag("li")) {
//                String tmp = row.select("a").attr("title"); //tmp is the chapter name
//                if(!tmp.startsWith("Chapter")) continue;
//
//                maxChap = Math.max(maxChap, Integer.parseInt(tmp.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
//                minChap = Math.min(minChap, Integer.parseInt(tmp.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        url = website+novel+chapter+".html";
//        try { //chapter web-scraping
//            Document doc = Jsoup.connect(url).get();
//            for(Element row:doc.getElementsByTag("p")) { //this is the light novel text
//                information+=row.text()+"\n";
//            }
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        String info[] = description.split("\\s+(?=(Genre:|Source:|Status:))");
//        for(int i=0;i<info.length;i++) {
//            if(info[i].startsWith("Author")) {
//                String tmp = info[i].replace("Author:", "");
//                tmp = tmp.replace(",", "");
//                System.out.println("Author: "+tmp);
//            } else if(info[i].startsWith("Genre")) {
//                String tmp[] = info[i].replace("Genre:", "").split("[,]");
//                System.out.println("Genre(s) include: "+ Arrays.toString(tmp));
//            }
//        }
//        System.out.println("Novel Thumbnail: "+website+thumbnail);
//        System.out.printf("Summary: %s\n", summary);
//        System.out.printf("Max chap: %d | Min chap: %d\n", maxChap, minChap);

//        System.out.println(html);
        new LightNovelTest();
    }
    static String x = "From the moment they entered the Red Sea, whispers and other communication systems were prohibited and the use of teleport-type magic was also blocked. Still, the Overgeared members were aware of Berith’s emergence.\n" +
            "\n" +
            "[22nd Great Demon Berith has appeared somewhere on the continent...!]\n" +
            "\n" +
            "It was thanks to the world message. Grid and the 10 meritorious retainers knew the strength of the great demon, so they were forced to feel nervous. Everything would be over if the great demon invaded the Overgeared Kingdom. It was the right move to return home now. There was a flurry of impatient and negative opinions.\n" +
            "\n" +
            "Lauel calmed them down, “I have been talking to Damian since the ceremony started.”\n" +
            "\n" +
            "Damian had told Lauel that if he couldn’t stop the great demon from ascending to the ground, he would lead the great demon as far away from the Overgeared Kingdom as possible. It was certainly possible. The battlefield between the Rebecca Church and Yatan Church was the furthest place from the Overgeared Kingdom. Damian always acted with the Overgeared Kingdom in mind. He was a really strong ally.\n" +
            "\n" +
            "‘However, I didn’t think he would fail to stop the ritual.’\n" +
            "\n" +
            "This summoning ritual was different from the one with Belial. Belial’s summoning ritual had been carried out in secret while Berith’s ritual had gotten exposed in the middle. The Rebecca Church had converged on the scene of the ritual. When he first heard the news, Lauel thought that Damian and the Rebecca Church would be able to stop the appearance of the great demon. The Rebecca Church was a force that obstructed the ambitions of the Yatan Church. It was expected they would have many means to prevent the summoning ritual.\n" +
            "\n" +
            "‘Yet they failed...? Was it not possible to stop the great demon ritual once it started?’ Lauel was filled with doubts.\n" +
            "\n" +
            "In the last few decades, how had the Rebecca Church been able to suppress the Yatan Church? Comparing the powers of the two religions, the Yatan Church was far stronger, and the Rebecca Church didn’t have the means to stop the great demon ritual. There was absolutely no reason for the Yatan Church to be suppressed by the Rebecca Church. However, in history, the Yatan Church had been consistently beaten by the Rebecca Church. They had worked secretly to avoid the eyes of the Rebecca Church.\n" +
            "\n" +
            "‘All nations on the continent support the Rebecca Church and are hostile to the Yatan Church...’\n" +
            "\n" +
            "Yet it wasn’t enough.\n" +
            "\n" +
            "‘It is likely there is a power hidden within the Rebecca Church.’\n" +
            "\n" +
            "It was a reasonable guess. Only with that could the balance be accepted.\n" +
            "\n" +
            "‘I’ll have to discuss it with Damian in detail one day. By the way...’ Lauel sealed off the complicated thoughts for a while and turned toward the sea again. All types of monsters appeared in these magical waters and were beaten by Grid and the 10 meritorious retainers.\n" +
            "\n" +
            "‘The route to the historical ruins itself is quite rewarding.’\n" +
            "\n" +
            "The rewards given by the sea monsters were quite good. The 10 meritorious retainers recovered the experience they had lost from dying several times in the war. They received all types of unusual items. It was hard to say if the items were good or bad, but they were estimated to be very valuable in terms of rarity.\n" +
            "\n" +
            "‘Grid has reached level 398... In the future, the Red Sea will be part of our hunting grounds.’\n" +
            "\n" +
            "It was hard to believe the day would come when the Red Sea was used as a hunting ground. This was possible thanks to Katz buying a warship. With a ship of this size, it wasn’t impossible to sail through the disaster-ridden Red Sea.\n" +
            "\n" +
            "“I can’t kill the octopuses when I look at them. They are like Vantner’s brothers.”\n" +
            "\n" +
            "“...” Vantner was wearing the octopus head. It was a remark that would make him furious if he had his original personality. Now he couldn’t say a word because the speaker was Jishuka.\n" +
            "\n" +
            "It was Yura as well. “Yes.”\n" +
            "\n" +
            "Considering Yura’s personality, she didn’t intend to make fun of him. She sincerely agreed with Jishuka’s words.\n" +
            "\n" +
            "“...” Therefore, Vantner couldn’t say anything.\n" +
            "\n" +
            "‘Their relationship is getting better.’ Grid had a warm expression on his face. The biggest harvest of this voyage was the relationship between Yura and her colleagues. Yura, who had been awkward with everyone, developed a more comfortable relationship with them during the journey. In particular, she seemed to have come to a consensus with Jishuka.\n" +
            "\n" +
            "“Doesn’t this make you realize that Grid is older than you?”\n" +
            "\n" +
            "It happened after a tough hunt of a whale that was overwhelmingly bigger than the octopuses, pufferfish, and so on. Yura and Jishuka sat next to each other and chatted in a relaxed manner. There was a consensus that they liked the same man, so the conversation didn’t stop.\n" +
            "\n" +
            "“Youngwoo-ssi has a tendency to get angry, but he doesn’t make fun of other people.”\n" +
            "\n" +
            "“I agree. He just looks at me like I am cute whenever I tease Vantner. It is a reaction that is shown when kids play well together. Ah... This score must be completely bad.” Jishuka was trying to be recognized as someone more than a friend, but instead she was like a child. This was a disaster. It was the end. She was afraid she would seem more like a sister and not a friend.\n" +
            "\n" +
            "“Dammit, Vantner... Why are you so funny?”\n" +
            "\n" +
            "“When?”\n" +
            "\n" +
            "“You and the octopus together is too funny.”\n" +
            "\n" +
            "“...” Vantner was tearful because of Jishuka’s words. Then the silent Yura spoke after agonizing over it. In fact, she didn’t want to say it. It was disadvantageous to her to give courage to Jishuka. Nevertheless, Yura confessed, “It is me, not you, who Youngwoo-ssi thinks of as a friend. Youngwoo-ssi’s eyes toward you are very different from when he looks at me. So don't worry too much.”\n" +
            "\n" +
            "“That’s right. He only sees my chest.” Jishuka spoke so seriously that Vantner found it funny.\n" +
            "\n" +
            "Then the usually calm and gentle Yura gritted her teeth. “Vantner, do you want to spar?”\n" +
            "\n" +
            "“...I’m sorry.”\n" +
            "\n" +
            "If they fought, he would lose. After all, Yura was a National Competition PvP finalist. He still vividly remembered it. A white giant with a classic name... Yura had fought fiercely against Zibal who boarded the magic machine, which had a different physical ability and vitality to players. She went against the predictions that she would lose easily. Yura was strong. She was one of the top five in the Overgeared Guild and several times stronger than Vantner.\n" +
            "\n" +
            "Vantner coughed and changed the topic, “By the way, what should we do if others catch the great demon?”\n" +
            "\n" +
            "There were many strong guilds beside the Overgeared Guild. Of course, they couldn’t compare to the Overgeared Guild that had numerous named NPCs. There was the Ares Army, the former Seven Guilds, and the forces that had been expanding their power since the collapse of the Seven Guilds. They might fail the great demon raid alone, but it might be different if they cooperated.\n" +
            "\n" +
            "“Grid, Mercedes, and Piaro, the three of them could raid something like the cave cricket which is on the level of a great demon raid. What about when hundreds of people join together to raid one great demon? It is a pity that we are handing over the great demon to others for a long time.”\n" +
            "\n" +
            "Other members of the 10 meritorious retainers agreed with Vantner’s statement. However, Grid was calm. “They won’t raid it.”\n" +
            "\n" +
            "When it came to the cave cricket and great demon, the premise of multiplying numbers was wrong. It was true that the physical abilities of the cave cricket were comparable to the great demon, but the skill level and intelligence was inferior to the great demons. Furthermore...\n" +
            "\n" +
            "“The high rankers can’t be compared to Mercedes and Piaro.”\n" +
            "\n" +
            "“...Ah.” Vantner scratched his head. The current Grid could kill hundreds of high rankers alone. Mercedes and Piaro were super-strong powers who could beat even Grid. Just because the three of them could raid the cave cricket didn’t mean that hundreds of high rankers could catch the great demon.\n" +
            "\n" +
            "\n" +
            "Lauel smiled. “We should let the people know it...”\n" +
            "\n" +
            "The public had been envious of the Overgeared Guild for monopolizing the rewards of the Belial raid. They should know.\n" +
            "\n" +
            "“...The fact that we have saved the world.”\n" +
            "\n" +
            "A great demon raid wasn’t a blessing. They had won after overcoming life and death. He couldn’t forget that sight of Piaro’s back as he prepared to die. A few days later...\n" +
            "\n" +
            "“We are arriving soon!” The captain shouted.\n" +
            "\n" +
            "Far away, they saw an island that was larger than expected. Surprisingly, the sky near the island was sunny. It was the first clear sky they had encountered during their 10 days of sailing. The Red Sea always had bursts of magic power and thick fog and storms occurring without a break.\n" +
            "\n" +
            "[You have found the Ruins of the War God!]\n" +
            "\n" +
            "“We’ve finally arrived...”\n" +
            "\n" +
            "“Apart from the coast, it’s all a jungle...? There is less open space. It will be tricky to use a greatsword.”\n" +
            "\n" +
            "“Based on my experience, the followers of the war god move lightly and quickly. Fighting in the jungle will be a disadvantage, so we should use the coast well.”\n" +
            "\n" +
            "Could they get some secret techniques here? The expectations of the 10 meritorious retainers swelled up.\n" +
            "\n" +
            "‘I think the relationship with the empire can be improved depending on the situation...’ Lauel considered the political part.\n" +
            "\n" +
            "Meanwhile, Grid was wondering about the sin of the war god. ‘It is pride?’\n" +
            "\n" +
            "Grid’s quest about the Seven Malignant Saints wasn’t related to the war god, but it didn’t prevent his curiosity. The war god was the strongest god, while pride was the ultimate sin among the seven sins.\n" +
            "\n" +
            "‘I think I’ll be screwed if we meet... Well, I don’t think it will happen.’\n" +
            "\n" +
            "It was the war god. He wasn’t a local neighbourhood dog that Grid could encounter wherever he went.\n" +
            "\n" +
            "\n" +
            "\n" +
            "***\n" +
            "\n" +
            "\n" +
            "“I have been burning for a long time.”\n" +
            "\n" +
            "“It is stimulating.”\n" +
            "\n" +
            "“That’s right.”\n" +
            "\n" +
            "Grenhal, Morse, and Basara—they had been suffering for the past fortnight. They protected Skunk from the followers while Skunk’s expedition group found stones and murals. Simultaneously, they encouraged the soldiers who were growing anxious and hunted sea monsters to secure food. The level of the ruins was too high, and they had to take care of all the important work and the chores.\n" +
            "\n" +
            "However, the dukes didn’t feel regret. They were willing to suffer. The experience erased the laziness caused from everyday life and provoked their enthusiasm.\n" +
            "\n" +
            "“I am too lacking... I’m sorry.”\n" +
            "\n" +
            "The number of times their lives were saved by the dukes couldn’t be counted. The dukes had suffered for a fortnight, and Skunk felt sorry for them. The level of his archaeological and decoding skills meant it was hard for him to solve the mystery of the ruins.\n" +
            "\n" +
            "On the first day, he knew that they needed a key for the exploration, but the method to obtain the key was still unknown. Skunk blamed himself for his incompetence, causing the dukes to suffer more.\n" +
            "\n" +
            "‘How much longer until I obtain a hint? I can’t guarantee it.’\n" +
            "\n" +
            "For a true exploration, they must break through the jungle. However, it was impossible to enter the jungle because there were tens of thousands of traps installed, as well as the war god followers. The keys to unlock the traps were absolutely necessary. They just didn’t know where to get the keys.\n" +
            "\n" +
            "“Don’t take it to heart. Many scholars and explorers have been sent from my country, and they will be a great strength to you.”\n" +
            "\n" +
            "“It is reassuring to hear. I understand. I will work hard.” Skunk felt more comfortable after hearing Grenhal’s words and finally relaxed. It meant the situation was so bad that Skunk welcomed the help of others despite enjoying the process of solving mysteries.\n" +
            "\n" +
            "Another day passed.\n" +
            "\n" +
            "Then the next day.\n" +
            "\n" +
            "“Uh...?” Skunk observed the contents of the mural he found two days ago and felt like he had been hit by a lightning bolt. His gaze was fixed on the booklet depicted in the mural. The booklet existed in every mural he found so far. Skunk had interpreted it as the symbol of the war god’s secret techniques. It was natural for it to be present in the murals of the Ruins of the War God. However, now it seemed more meaningful.\n" +
            "\n" +
            "‘I thought the reason for the booklets being drawn differently in every mural was that they depicted different types of secret techniques...’\n" +
            "\n" +
            "The shape of the keyhole passed through Skunk’s mind. He crossed it with the booklets drawn on the seven murals he had found so far. It fit perfectly with the shape of the keyhole.\n" +
            "\n" +
            "‘The key is divided into pieces.’\n" +
            "\n" +
            "The murals were a hint telling him where the pieces of the key were.\n" +
            "\n" +
            "‘Good.’\n" +
            "\n" +
            "Skunk clenched his fists. He knew the exact meaning of the murals and hoped the interpretation would make things easier.\n" +
            "\n" +
            "© Copyright NovelFull.Com. All Rights Reserved.\n" +
            "\n";
}
