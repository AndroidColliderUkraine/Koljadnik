package com.androidcollider.koljadnik.utils;

public class ChordUtils {

    public final static String CHORD_TAG_OPEN = "<chord>";
    public final static String CHORD_TAG_CLOSE = "</chord>";

    private static String[] major = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "H"};
    private static String[] minor = new String[]{"Cm", "C#m", "Dm", "D#m", "Em", "Fm", "F#m", "Gm", "G#m", "Am", "A#m", "Hm"};
    private static String[] sept = new String[]{"C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "A#7", "H7"};

    private static String[] major_b = new String[]{"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
    private static String[] minor_b = new String[]{"Cm", "Dbm", "Dm", "Ebm", "Em", "Fm", "Gbm", "Gm", "Abm", "Am", "Bbm", "Bm"};
    private static String[] sept_b = new String[]{"C7", "Db7", "D7", "Eb7", "E7", "F7", "Gb7", "G7", "Ab7", "A7", "Bb7", "B7"};

    private static String[] major_sus2 = new String[]{"Csus2", "C#sus2", "Dsus2", "D#sus2", "Esus2", "Fsus2", "F#sus2", "Gsus2", "G#sus2", "Asus2", "A#sus2", "Hsus2"};
    private static String[] minor_sus2 = new String[]{"Cmsus2", "C#msus2", "Dmsus2", "D#msus2", "Emsus2", "Fmsus2", "F#msus2", "Gmsus2", "G#msus2", "Amsus2", "A#msus2", "Hmsus2"};
    private static String[] sept_sus2 = new String[]{"C7sus2", "C#7sus2", "D7sus2", "D#7sus2", "E7sus2", "F7sus2", "F#7sus2", "G7sus2", "G#7sus2", "A7sus2", "A#7sus2", "H7sus2"};

    private static String[] major_sus4 = new String[]{"Csus4", "C#sus4", "Dsus4", "D#sus4", "Esus4", "Fsus4", "F#sus4", "Gsus4", "G#sus4", "Asus4", "A#sus4", "Hsus4"};
    private static String[] minor_sus4 = new String[]{"Cmsus4", "C#msus4", "Dmsus4", "D#msus4", "Emsus4", "Fmsus4", "F#msus4", "Gmsus4", "G#msus4", "Amsus4", "A#msus4", "Hmsus4"};
    private static String[] sept_sus4 = new String[]{"C7sus4", "C#7sus4", "D7sus4", "D#7sus4", "E7sus4", "F7sus4", "F#7sus4", "G7sus4", "G#7sus4", "A7sus4", "A#7sus4", "H7sus4"};

    private static String changeAccord(String[] chord_array, String text, int direction) {

        StringBuffer textBuffer = new StringBuffer(text);
        int p = 0;
        while (p != -1) {
            p = -1;
            for (int i = 0; i < chord_array.length; i++) {
                String for_find = CHORD_TAG_OPEN + chord_array[i] + CHORD_TAG_CLOSE;
                int a = textBuffer.indexOf(for_find, 0);// Находить перше входження рядка for_find в рядку in_str, починают з 0 символу
                if (a >= 0) {
                    p = a;
                    String next;
                    if (direction > 0) {
                        if (i == 11) {
                            next = chord_array[0];
                        } else {
                            next = chord_array[i + 1];
                        }
                    } else {
                        if (i == 0) {
                            next = chord_array[11];
                        } else {
                            next = chord_array[i - 1];
                        }
                    }
                    next = "$" + next;         //Добавляємо симлов для уникнення повторного транспонування
                    textBuffer.delete(a + CHORD_TAG_OPEN.length(), a + CHORD_TAG_OPEN.length() + chord_array[i].length());//Видаляє попередній акорд
                    textBuffer.insert(a + CHORD_TAG_OPEN.length(), next);  //  Вставляє новий акорд
                }
            }
        }
        return (new String(textBuffer));
    }

    public static String downAccord(String text) {

        text = changeAccord(major, text, -1);
        text = changeAccord(minor, text, -1);
        text = changeAccord(sept, text, -1);

        text = changeAccord(major_b, text, -1);
        text = changeAccord(minor_b, text, -1);
        text = changeAccord(sept_b, text, -1);

        text = changeAccord(major_sus2, text, -1);
        text = changeAccord(minor_sus2, text, -1);
        text = changeAccord(sept_sus2, text, -1);

        text = changeAccord(major_sus4, text, -1);
        text = changeAccord(minor_sus4, text, -1);
        text = changeAccord(sept_sus4, text, -1);


        String result = text.replace("$", "");
        return result;
    }

    public static String upAccord(String text) {

        text = changeAccord(major, text, 1);
        text = changeAccord(minor, text, 1);
        text = changeAccord(sept, text, 1);

        text = changeAccord(major_b, text, 1);
        text = changeAccord(minor_b, text, 1);
        text = changeAccord(sept_b, text, 1);

        text = changeAccord(major_sus2, text, 1);
        text = changeAccord(minor_sus2, text, 1);
        text = changeAccord(sept_sus2, text, 1);

        text = changeAccord(major_sus4, text, 1);
        text = changeAccord(minor_sus4, text, 1);
        text = changeAccord(sept_sus4, text, 1);


        String result = text.replace("$", "");
        return result;
    }
}