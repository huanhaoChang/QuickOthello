package jp.co.nissan.ae.quickothello.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.nissan.ae.quickothello.model.ComputerPlayer
import jp.co.nissan.ae.quickothello.model.GameLogic
import jp.co.nissan.ae.quickothello.model.OthelloGameLogic
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {

    @Provides
    @Singleton
    fun provideGameLogic(): GameLogic {
        return OthelloGameLogic()
    }

    @Provides
    @Singleton
    fun provideOthelloGameLogic(): OthelloGameLogic {
        return OthelloGameLogic()
    }

    @Provides
    @Singleton
    fun provideComputerPlayer(gameLogic: OthelloGameLogic): ComputerPlayer {
        return ComputerPlayer(gameLogic)
    }
}