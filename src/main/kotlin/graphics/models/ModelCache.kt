package graphics.models

import resources.Cache

object ModelCache: Cache<Model>(ModelLoader())