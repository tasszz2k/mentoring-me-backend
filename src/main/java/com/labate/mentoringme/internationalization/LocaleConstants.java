package com.labate.mentoringme.internationalization;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LocaleConstants {
  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  public static final Map<String, Locale> SUPPORT_LOCALES =
      Stream.of(DEFAULT_LOCALE, new Locale("vi"))
          .collect(Collectors.toMap(Locale::getLanguage, locale -> locale));
}
