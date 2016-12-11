package com.androidcollider.koljadnik.root;

import android.app.Application;

import com.androidcollider.koljadnik.song_types.SongType;
import com.androidcollider.koljadnik.songs.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of ${CLASS_NAME}
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.androidcollider.koljadnik
 */
public class App extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        List<SongType> songTypes = new ArrayList<>();
        songTypes.add(new SongType(1, "Українські"));
        songTypes.add(new SongType(2, "Іноземні"));

        List<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "Коляд, коляд, колядниця", 15, 1, "Коляд, коляд, колядниця,\n" +
                "Добра з маком паляниця,\n" +
                "А без маку не така,\n" +
                "Дайте, дядьку, п’ятака,\n" +
                "Як не дасте п’ятака,\n" +
                "Возьму вола за рога.\n" +
                "Сюди ріг, туди ріг,\n" +
                "Послав батько по пиріг.\n" +
                "Казав: \"Не барися,\n" +
                "Пирогом ділися\".\n", null, "(\"Добрий вечір!\" – промовляючи)", null));

        songs.add(new Song(2, "Ірод цар за Христом ганявся", 3, 1, "\n" +
                "Удвох:\n" +
                "Він за ним дуже побивався.\n" +
                "\n" +
                "Всі швиденько:\n" +
                "На сідельці не вдержався,\n" +
                "З кобильчини він зірвався.\n" +
                "\n" +
                "маленька пауза, повільно:\n" +
                "Та й упав на діл.\n" +
                "\n" +
                "(Повторити три останні рядки)\n" +
                "Іродиха, як теє зачула,\n" +
                "Що зірвалась у царя підпруга,\n" +
                "Схопилася з ліжка боса\n" +
                "І як є простоволоса\n" +
                "Йому навздогін.\n" +
                "Він лежить, ледве дух одводить,\n" +
                "Кобильчиха\n" +
                "Кругом нього ходить,\n" +
                "Йому в очі заглядає,\n" +
                "Хвостом мухи одганяє.\n" +
                "Ще й приска на вид!\n" +
                "\n", null, "", null));

        songs.add(new Song(3, "А в горі, горі", 29, 1, "А в горі, в горі, в глибокім зворі.\n" +
                "\n" +
                "Приспів:\n" +
                "Славен єс, Славен єс, Боже.\n" +
                "\n" +
                "Росте деревце, тонке, високе.\n" +
                "Тонке, високе, а вшир широке.\n" +
                "А вшир широке, а ввись кудряве.\n" +
                "А на тім кудрі сам сокіл сидить.\n" +
                "Сам сокіл сидить, далеко й видить.\n" +
                "Видить в Николи... тисові столи.\n" +
                "Як вконець стола сидить Никола.\n" +
                "Ой сидить, сидить, слізоньку ронить.\n" +
                "А з тої слізки сталося й море.\n" +
                "А по тім морю пливе й корабель.\n" +
                "А в тім кораблі тисові столи.\n" +
                "Позастелені, чом скатерками.\n" +
                "Чом скатерками, чом дорогими.\n" +
                "А на тих столах дороге й пиття.\n" +
                "Медок-солодок й солодка кутя.\n" +
                "Та за цим столом будь же нам здоров!\n" +
                "Віншуєм тебе щастям, здоров’ям.\n" +
                "Щастям, здоров’ям та з цими святми.\n" +
                "Та з цими святми, чом Різдвяними.\n" +
                "Чом Різдвяними, чом роковими.\n" +
                "Ой здоров, здоров та будь сам з собов.\n" +
                "З свойов газдинев та з діточками.\n" +
                "Та з діточками, та з сусідками.*\n" +
                "Та з усім родом – близьким й далеким.\n" +
                "Близьким й далеким, великим й малим.\n" +
                "Ой дай ти, Боже, з поля доволі.\n" +
                "З поля доволі, а в дім здоров’я.\n" +
                "На челядочку, на худібочку.\n" +
                "Ой що ж ми тобі колядували.\n" +
                "Колядували, красно співали.\n" +
                "Як перепілка в ярій пшениці.\n" +
                "Як ластівонька в новенькій стрісі.\n" +
                "Як соловейко та в лузі, лузі.\n" +
                "Честь Богу, хвала! Навіки слава!\n" +
                "Навіки слава! Навіки слава!", null, "1. Пісні з Галичини / Упорядники Р.П. Береза, М.О. Дацко. – Львів: Світ, 1997. – 192 с.\n" +
                "2. Золотий Дунай. Символіка української пісні / Упорядник Марія Чумарна. – Тернопіль: Навчальна книга – Богдан, 2007. – 264 с.", null));

        songs.add(new Song(4, "Jingle Bells", 3, 1, "Dashing through the snow\n" +
                "In a one-horse open sleigh\n" +
                "O'er the fields we go\n" +
                "Laughing all the way\n" +
                "\n" +
                "Bells on bob tail ring\n" +
                "Making spirits bright\n" +
                "What fun it is to ride and sing\n" +
                "A sleighing song tonight!\n" +
                "\n" +
                "Jingle bells, jingle bells,\n" +
                "Jingle all the way.\n" +
                "Oh! what fun it is to ride\n" +
                "In a one-horse open sleigh.\n" +
                "\n" +
                "Jingle bells, jingle bells,\n" +
                "Jingle all the way;\n" +
                "Oh! what fun it is to ride\n" +
                "In a one-horse open sleigh.", null, "James Lord Pierpont", null));
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
