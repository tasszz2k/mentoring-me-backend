package com.labate.mentoringme.internationalization;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

  public LocaleConfig() {
    super();
    setDefaultLocale(LocaleConstants.DEFAULT_LOCALE);
    setSupportedLocales(new ArrayList<>(LocaleConstants.SUPPORT_LOCALES.values()));
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageResource =
        new ReloadableResourceBundleMessageSource();
    messageResource.setBasenames("classpath:i18n/messages");
    messageResource.setDefaultEncoding("UTF-8");
    messageResource.setCacheSeconds(60);
    Locale.setDefault(LocaleConstants.DEFAULT_LOCALE);
    return messageResource;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
    localeInterceptor.setParamName("lang");
    registry.addInterceptor(localeInterceptor);
  }

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
    if (!StringUtils.hasText(language)) {
      return getDefaultLocale();
    }
    List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(language);
    Locale foundLocale = Locale.lookup(languageRanges, getSupportedLocales());
    if (foundLocale == null) return getDefaultLocale();
    return foundLocale;
  }
}
