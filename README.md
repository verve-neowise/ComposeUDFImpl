# Android Compose UDF Implementation

![Work](screenshots/UDF.jpg)

**State** - Состояние UI

**Action** - действия отправляемые в Reducer (ViewModel)

**Event** - события для UI  (не меняет State, можно использовать для событий навигации)

**Effect** - Побочные эффекты (Асинхронные, Многопоточные операции), вроде отправки запросов на сервер и тп (не меняет State)

**Reducer** (ViewModel, метод dispatch) - Получает действия (Action) с UI или Platform (Android System) или Effector. Передает Action и текущий State в reduce. Reducer тем самым обновляет State на полученный и запускает полученные эффекты передав в Effector

**Effector** - рантайм эффектов, запускает полученные эффекты асинхронно. После завершения эффект может вернуть Action, который передается в Reducer для обработки

**reduce** - получает Action и State, синхронно применяет action к state и возращает обновленный State и список Side Effect'ов в Reducer. Так же может производить events

**Slice** - часть состояния и логики, которая может быть подключена к UDFViewModel

# UDFViewModel


Contract
```kotlin

data class State(
    val isLoading: Boolean = false,
    val content: String = ""
)

sealed class Action {
    object LoadContent : Action()
    data class UpdateContent(val content: String) : Action()
}

sealed class Effect {
    object FetchData : Effect()
}

```

Activity
```kotlin
viewModel.dispatch(Action.LoadContent)
```

ViewModel
```kotlin
fun ReducerScope.reduce(action: Action, state: State): State = when(action) {
    is Action.LoadContent -> {
        sendEffect(Effect.FetchData)
        state.copy(isLoading = true)
    }
    is Action.UpdateContent -> {
        state.copy(isLoading = false, content = action.content)
    }
    else -> {
        ActionNotDispatched(action)
    }
}

fun EffectorScope.affect(effect: Effect) {
    when(effect) {
        is Effect.FetchData -> {
            fetchDataUseCase().collect { content ->
                dispatch(UpdateContent(content = content))
            }
        }
        else -> {
            EffectNotDispatched(effect)
        }
    }
}
```

UI

Compose

```kotlin
@Composable 
fun UI(
    state: State,
    events: EventsHolder<UDF.Event>,
    dispatch: (UDF.Action) -> Unit
) {

    val context = LocalContext.current

    OnEvent(events = events) { event ->
        when(event) {
            is ShowMessage -> showToast(context, event.message)
        }
    }

    UserProfile(
        user = state.user,
        isLoading = state.isLoading
    )
    
    DetailContent(
        content = state.content
    )

    Button(
        onClick = {
            dispatch(Action.LoadContent)
        }
    ) {
        Text("Load Details")
    }
}
```

# Slices

UDFViewModel умеет диспатчить события во все подключенные Slices и обновлять состояние

Create contract
```kotlin
class State(
    val isLoading: Boolean = false,
    val content: String = ""
)

sealed class Action {
    object LoadContent : Action()
    data class UpdateContent(val content: String) : Action()
}

sealed class Effect {
    object FetchData : Effect()
}
```

Add state to Root State
```kotlin
data class RootState(
    val userProfileState: State,
    // ...
)
```

Create slice

```kotlin
class UserProfileSlice : Slice<State> {
    fun ReducerScope.reduce(action: Action, state: State): State = when(action) {
        is Action.LoadContent -> {
            sendEffect(Effect.FetchData)
            state.copy(isLoading = true)
        }
        is Action.UpdateContent -> {
            state.copy(isLoading = false, content = action.content)
        }
        else -> {
            ActionNotDispatched(action)
        }
    }
    
    fun EffectorScope.affect(effect: Effect) {
        when(effect) {
            is Effect.FetchData -> {
                fetchDataUseCase().collect { content ->
                    dispatch(UpdateContent(content = content))
                }
            }
            else -> {
                EffectNotDispatched(effect)
            }
        }
    }
}
```

## Add slice to viewModel

```kotlin

val userProfileSlice = UserProfileSlice()

init {
    viewModel.use(userProfileSlice, { state.userProfileState }, { state, userProfileState ->
        state.copy(userProfileState = userProfileState)
    })
}
```