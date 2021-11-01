
# Учебное приложение job4j_url_shortcut

Данное приложение создает сервис, позволяющий зарегистрированным сайтам получать короткие ссылки на свои страницы 
и осуществлять переход на них по этим коротким ссылкам с одновременной фиксацией количества вызовов этих страниц. 
С помощью специального запроса можно получать статистику по количествам вызовов страниц.


Приложение было создано в рамках прохождения онлайн-курса обучения [job4j.ru](https://job4j.ru)


[![Build Status](https://app.travis-ci.com/vsolomatoff/job4j_url_shortcut.svg?branch=master)](https://app.travis-ci.com/vsolomatoff/job4j_url_shortcut)
[![codecov](https://codecov.io/gh/vsolomatoff/job4j_url_shortcut/branch/master/graph/badge.svg?token=1cuMQeJKjG)](https://codecov.io/gh/vsolomatoff/job4j_url_shortcut)


# Сборка приложения

В приложении используется база данных PostgreSQL в качестве хранилища данных.
Вы должны иметь у себя установленный инстенс PostgreSQL и самостоятельно создать в нем базу данных c именем shortcut.

Например, используя команду:
```yml
CREATE DATABASE shortcut;
```

Для сборки приложения используется сборщик maven.

Вы можете просто запустить команду:
```yml
mvn install
```

# Запуск приложения (сервиса)

Запустить приложение Вы можете с помощью следующей команды:
```yml
java -jar job4j_url_shortcut-1.0.0.jar
```

# Использование сервиса:

Документацию по API сервиса Вы можете найти по ссылке
[Документация по API](https://app.swaggerhub.com/apis-docs/vsolomatoff/url-shortcut_system_api/1.0.0)




