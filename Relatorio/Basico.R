library(tidyverse)
library(readr)
resumo <- read_csv("resumo.csv")
Parsed with column specification:
  cols(
    Identificação = col_character(),
    QNT_Quantum = col_integer(),
    MediaTrocas = col_double(),
    MediaIntrucoes = col_double(),
    Tipo = col_character()
  )
x <- ggplot(resumo)
attach(resumo)
> x + geom_point(aes(x = QNT_Quantum, y = MediaTrocas))
x + geom_smooth(aes(x = QNT_Quantum, y = MediaIntrucoes, color = "blue")) + 
  geom_smooth(aes(x = QNT_Quantum, y = MediaTrocas, color ="red")) + 
  ggtitle("Tamanho do Quantum por Médias") + xlab("Tamanho do Quantum") + ylab("Médias")