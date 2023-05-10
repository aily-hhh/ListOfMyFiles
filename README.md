# ListOfMyFiles
Приложение, отображающее список файлов на устройстве


# Используемый стек:
  ♥ Kotlin
  ♥ Coroutine
  ♥ LiveData
  ♥ Hilt
  ♥ XML
  ♥ MVVM
  ♥ Single Activity


# Список задач
♥ Activity (главный экран)
    • При каждом запуске приложения (обновления списка файлов?) в фоне должны сохраняться хеш-коды; (-)
    • По умолчанию отсортирован по названию (возрастание?); (+)
    • Кнопки сортировки по возрастанию/убыванию; (+)
    • Выбор элемента сортировки (размер, дата создания, расширение); (+)
    • Кнопка, которая будет отображать только измененные файлы; (-)
    • При нажатии на элемент с id=sortTextView открывается bottomSheet с выбором сортировки; (+)
    • Иконка для каждого формата файла; (+)
    • Поделиться файлом (long click on item); (+)
    • Открыть файл в другом приложении, которое поддерживает данный файл (click on item); (+)
    
    
# Вопросы
  ♥ Отображение только файлов из внешнего хранилища или в том числе и директорий с файлами, по которым можно перемещаться?


# Итог
  • В списке отображаются файлы из внешнего хранилища в папке Downloads
  • По нажатию файл можно открыть в другом приложении
  • По долгому нажатию файлом можно поделиться
  • Фильтры сортировки реализованы с помощью bottom sheet dialog
    
