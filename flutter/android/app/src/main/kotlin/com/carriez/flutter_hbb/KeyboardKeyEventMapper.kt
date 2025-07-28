package hbb;
import android.view.KeyEvent
import android.view.KeyCharacterMap
import hbb.MessageOuterClass.KeyboardMode
import hbb.MessageOuterClass.ControlKey

object KeyEventConverter {
    fun toAndroidKeyEvent(keyEventProto: hbb.MessageOuterClass.KeyEvent): KeyEvent {
        var chrValue = 0
        var modifiers = 0

        val keyboardMode = keyEventProto.getMode()

        if (keyEventProto.hasChr()) {
            val chr = keyEventProto.getChr().toChar()
            chrValue = charToKeyCode(chr)
            // Если заглавная буква, добавляем модификатор Shift
            if (chr.isUpperCase() || isShiftRequired(chr)) {
                modifiers = modifiers or KeyEvent.META_SHIFT_ON
            }
        } else if (keyEventProto.hasControlKey()) {
            chrValue = convertControlKeyToKeyCode(keyEventProto.getControlKey())
        }

        var modifiersList = keyEventProto.getModifiersList()

        if (modifiersList != null) {
            for (modifier in keyEventProto.getModifiersList()) {
                val modifierValue = convertModifier(modifier)
                modifiers = modifiers or modifierValue
            }
        }

        var action = 0
        if (keyEventProto.getDown()) {
            action = KeyEvent.ACTION_DOWN
        } else {
            action = KeyEvent.ACTION_UP
        }

        return KeyEvent(0, 0, action, chrValue, 0, modifiers)
    }

    private fun convertModifier(controlKey: hbb.MessageOuterClass.ControlKey): Int {
        // Add logic to map ControlKey values to Android KeyEvent key codes.
        // You'll need to provide the mapping for each key.
        return when (controlKey) {
            ControlKey.Alt -> KeyEvent.META_ALT_ON
            ControlKey.Control -> KeyEvent.META_CTRL_ON
            ControlKey.CapsLock -> KeyEvent.META_CAPS_LOCK_ON
            ControlKey.Meta -> KeyEvent.META_META_ON
            ControlKey.NumLock -> KeyEvent.META_NUM_LOCK_ON
            ControlKey.RShift -> KeyEvent.META_SHIFT_RIGHT_ON
            ControlKey.Shift -> KeyEvent.META_SHIFT_ON
            ControlKey.RAlt -> KeyEvent.META_ALT_RIGHT_ON
            ControlKey.RControl -> KeyEvent.META_CTRL_RIGHT_ON
            else -> 0 // Default to unknown.
        }
    }

    private val tag = "KeyEventConverter"

    private fun convertUnicodeToKeyCode(unicode: Int): Int {
        val charMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD)
        android.util.Log.d(tag, "unicode: $unicode")
        val events = charMap.getEvents(charArrayOf(unicode.toChar()))
        if (events != null && events.size > 0) {
            android.util.Log.d(tag, "keycode ${events[0].keyCode}")
            return events[0].keyCode
        }
        return 0
    }

    private fun convertControlKeyToKeyCode(controlKey: hbb.MessageOuterClass.ControlKey): Int {
        // Add logic to map ControlKey values to Android KeyEvent key codes.
        // You'll need to provide the mapping for each key.
        return when (controlKey) {
            ControlKey.Alt -> KeyEvent.KEYCODE_ALT_LEFT
            ControlKey.Backspace -> KeyEvent.KEYCODE_DEL
            ControlKey.Control -> KeyEvent.KEYCODE_CTRL_LEFT
            ControlKey.CapsLock -> KeyEvent.KEYCODE_CAPS_LOCK
            ControlKey.Meta -> KeyEvent.KEYCODE_META_LEFT
            ControlKey.NumLock -> KeyEvent.KEYCODE_NUM_LOCK
            ControlKey.RShift -> KeyEvent.KEYCODE_SHIFT_RIGHT
            ControlKey.Shift -> KeyEvent.KEYCODE_SHIFT_LEFT
            ControlKey.RAlt -> KeyEvent.KEYCODE_ALT_RIGHT
            ControlKey.RControl -> KeyEvent.KEYCODE_CTRL_RIGHT
            ControlKey.DownArrow -> KeyEvent.KEYCODE_DPAD_DOWN
            ControlKey.LeftArrow -> KeyEvent.KEYCODE_DPAD_LEFT
            ControlKey.RightArrow -> KeyEvent.KEYCODE_DPAD_RIGHT
            ControlKey.UpArrow -> KeyEvent.KEYCODE_DPAD_UP
            ControlKey.End -> KeyEvent.KEYCODE_MOVE_END
            ControlKey.Home -> KeyEvent.KEYCODE_MOVE_HOME
            ControlKey.PageUp -> KeyEvent.KEYCODE_PAGE_UP
            ControlKey.PageDown -> KeyEvent.KEYCODE_PAGE_DOWN
            ControlKey.Insert -> KeyEvent.KEYCODE_INSERT
            ControlKey.Escape -> KeyEvent.KEYCODE_ESCAPE
            ControlKey.F1 -> KeyEvent.KEYCODE_F1
            ControlKey.F2 -> KeyEvent.KEYCODE_F2
            ControlKey.F3 -> KeyEvent.KEYCODE_F3
            ControlKey.F4 -> KeyEvent.KEYCODE_F4
            ControlKey.F5 -> KeyEvent.KEYCODE_F5
            ControlKey.F6 -> KeyEvent.KEYCODE_F6
            ControlKey.F7 -> KeyEvent.KEYCODE_F7
            ControlKey.F8 -> KeyEvent.KEYCODE_F8
            ControlKey.F9 -> KeyEvent.KEYCODE_F9
            ControlKey.F10 -> KeyEvent.KEYCODE_F10
            ControlKey.F11 -> KeyEvent.KEYCODE_F11
            ControlKey.F12 -> KeyEvent.KEYCODE_F12
            ControlKey.Space -> KeyEvent.KEYCODE_SPACE
            ControlKey.Tab -> KeyEvent.KEYCODE_TAB
            ControlKey.Return -> KeyEvent.KEYCODE_ENTER
            ControlKey.Delete -> KeyEvent.KEYCODE_FORWARD_DEL
            ControlKey.Clear -> KeyEvent.KEYCODE_CLEAR
            ControlKey.Pause -> KeyEvent.KEYCODE_BREAK
            else -> 0 // Default to unknown.
        }
    }

    // Новая функция: сопоставление символа с keyCode
    private fun charToKeyCode(c: Char): Int {
        return when (c) {
            in 'a'..'z' -> KeyEvent.KEYCODE_A + (c - 'a')
            in 'A'..'Z' -> KeyEvent.KEYCODE_A + (c - 'A')
            in '0'..'9' -> KeyEvent.KEYCODE_0 + (c - '0')
            ' ' -> KeyEvent.KEYCODE_SPACE
            '\n' -> KeyEvent.KEYCODE_ENTER
            '\t' -> KeyEvent.KEYCODE_TAB
            '!' -> KeyEvent.KEYCODE_1 // Shift+1
            '@' -> KeyEvent.KEYCODE_2 // Shift+2
            '#' -> KeyEvent.KEYCODE_3 // Shift+3
            '$' -> KeyEvent.KEYCODE_4 // Shift+4
            '%' -> KeyEvent.KEYCODE_5 // Shift+5
            '^' -> KeyEvent.KEYCODE_6 // Shift+6
            '&' -> KeyEvent.KEYCODE_7 // Shift+7
            '*' -> KeyEvent.KEYCODE_8 // Shift+8
            '(' -> KeyEvent.KEYCODE_9 // Shift+9
            ')' -> KeyEvent.KEYCODE_0 // Shift+0
            '-' -> KeyEvent.KEYCODE_MINUS
            '_' -> KeyEvent.KEYCODE_MINUS // Shift+Minus
            '=' -> KeyEvent.KEYCODE_EQUALS
            '+' -> KeyEvent.KEYCODE_EQUALS // Shift+Equals
            '[' -> KeyEvent.KEYCODE_LEFT_BRACKET
            '{' -> KeyEvent.KEYCODE_LEFT_BRACKET // Shift+Left Bracket
            ']' -> KeyEvent.KEYCODE_RIGHT_BRACKET
            '}' -> KeyEvent.KEYCODE_RIGHT_BRACKET // Shift+Right Bracket
            '\\' -> KeyEvent.KEYCODE_BACKSLASH
            '|' -> KeyEvent.KEYCODE_BACKSLASH // Shift+Backslash
            ';' -> KeyEvent.KEYCODE_SEMICOLON
            ':' -> KeyEvent.KEYCODE_SEMICOLON // Shift+Semicolon
            '\'' -> KeyEvent.KEYCODE_APOSTROPHE
            '"' -> KeyEvent.KEYCODE_APOSTROPHE // Shift+Apostrophe
            ',' -> KeyEvent.KEYCODE_COMMA
            '<' -> KeyEvent.KEYCODE_COMMA // Shift+Comma
            '.' -> KeyEvent.KEYCODE_PERIOD
            '>' -> KeyEvent.KEYCODE_PERIOD // Shift+Period
            '/' -> KeyEvent.KEYCODE_SLASH
            '?' -> KeyEvent.KEYCODE_SLASH // Shift+Slash
            '`' -> KeyEvent.KEYCODE_GRAVE
            '~' -> KeyEvent.KEYCODE_GRAVE // Shift+Grave
            else -> 0
        }
    }

    // Новая функция: требуется ли Shift для символа
    private fun isShiftRequired(c: Char): Boolean {
        return when (c) {
            in 'A'..'Z', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', '|', ':', '"', '<', '>', '?', '~' -> true
            else -> false
        }
    }
}
