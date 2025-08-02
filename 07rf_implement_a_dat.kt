package com.rf.integrator

import kotlin.reflect.KClass

interface DataLoader<T> {
    fun load(): List<T>
}

interface AutomationScript<T> {
    fun execute(data: List<T>)
}

class ScriptIntegrator<T : Any>(
    private val dataLoader: DataLoader<T>,
    private val script: AutomationScript<T>
) {
    fun integrate() {
        val data = dataLoader.load()
        script.execute(data)
    }
}

class ScriptRegistry {
    private val scripts: MutableMap<String, AutomationScript<*>> = mutableMapOf()

    fun registerScript(name: String, script: AutomationScript<*>) {
        scripts[name] = script
    }

    fun getScript(name: String): AutomationScript<*>? {
        return scripts[name]
    }
}

data class AutomationConfig<T : Any>(
    val dataSource: String,
    val scriptName: String,
    val dataClass: KClass<T>
)

class AutomationEngine {
    private val scriptRegistry: ScriptRegistry = ScriptRegistry()

    fun configure(config: AutomationConfig<*>) {
        val dataLoader = createDataLoader(config.dataSource, config.dataClass)
        val script = scriptRegistry.getScript(config.scriptName)
        if (script != null) {
            val integrator = ScriptIntegrator(dataLoader, script)
            integrator.integrate()
        } else {
            throw RuntimeException("Script not found: ${config.scriptName}")
        }
    }

    private fun createDataLoader(dataSource: String, dataClass: KClass<*>): DataLoader<*> {
        // implement data loader creation logic based on data source and class
        throw NotImplementedError()
    }
}