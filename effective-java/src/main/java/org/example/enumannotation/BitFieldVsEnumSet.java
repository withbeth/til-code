package org.example.enumannotation;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class BitFieldVsEnumSet {

    // 비트 필드
    static class TextUsingBitField {
        public static final int STYLE_BOLD   = 1 << 0; // 1
        public static final int STYLE_ITALIC = 1 << 1; // 2

        private int styles = 0;

        /**
         * @param styles param0개 이상의 STYLE상수를 비트별 OR한 값
         */
        public void applyStyles(int styles) {
            this.styles = styles;
        }
    }

    // EnumSet으로 해당 Text 구현
    static class TextUsingEnumSet {
        public enum Style {
            BOLD, ITALIC
        }

        private Set<Style> styles = new HashSet<>();

        public void applyStyle(Set<Style> styles) {
            this.styles = styles;
        }
    }

    public static void main(String[] args) {
        // AS-IS
        TextUsingBitField textUsingBitField = new TextUsingBitField();
        textUsingBitField.applyStyles(
                TextUsingBitField.STYLE_BOLD
                | TextUsingBitField.STYLE_ITALIC);

        // TO-BE
        TextUsingEnumSet textUsingEnumSet = new TextUsingEnumSet();
        textUsingEnumSet.applyStyle(EnumSet.of(
                TextUsingEnumSet.Style.BOLD,
                TextUsingEnumSet.Style.ITALIC
        ));

    }



}
