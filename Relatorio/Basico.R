library(tidyverse)
library(readr)
resumo <- read_csv("resumo.csv")
resumoProcessos <- read_csv("resumoProcessos.csv")

resumoNovosProcessos <- read_csv("resumoNovosProcessos.csv")
 
X0 <- read_csv("entradasTeste/0.csv")

X1 <- read_csv("entradasTeste/1.csv")

X2 <- read_csv("entradasTeste/2.csv")

X3 <- read_csv("entradasTeste/3.csv")

X4 <- read_csv("entradasTeste/4.csv")

X5 <- read_csv("entradasTeste/5.csv")

X6 <- read_csv("entradasTeste/6.csv")

X7 <- read_csv("entradasTeste/7.csv")

X8 <- read_csv("entradasTeste/8.csv")

X9 <- read_csv("entradasTeste/9.csv")

x <- ggplot(resumoProcessos)
attach(resumoProcessos)
x + geom_line(aes(x = QNT_Quantum, y = MediaIntrucoes, color = "Média de Instruções")) + 
  geom_line(aes(x = QNT_Quantum, y = MediaTrocas, color ="Média de Trocas")) + 
  ggtitle("Tamanho do Quantum por Médias") + xlab("Tamanho do Quantum") + ylab("Médias")
x <- ggplot(resumoNovosProcessos)
attach(resumoNovosProcessos)
x + geom_line(aes(x = QNT_Quantum, y = MediaIntrucoes, color = "Média de Instruções")) + 
  geom_line(aes(x = QNT_Quantum, y = MediaTrocas, color ="Média de Trocas")) + 
  ggtitle("Tamanho do Quantum por Médias") + xlab("Tamanho do Quantum") + ylab("Médias")
