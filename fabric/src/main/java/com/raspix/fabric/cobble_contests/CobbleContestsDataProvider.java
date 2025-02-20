package com.raspix.fabric.cobble_contests;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.data.DataProvider;
import com.cobblemon.mod.common.api.data.DataRegistry;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.util.perf.Profiler;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.cobblemon.mod.common.util.DistributionUtilsKt.server;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;
import static java.util.Collections.emptyList;

public class CobbleContestsDataProvider implements DataProvider {

    private boolean canReload = true;
    // Both Forge n Fabric keep insertion order so if a registry depends on another simply register it after
    private Set<DataRegistry> registries = new HashSet<>();
    private List<UUID> synchronizedPlayerIds = new LinkedList<>();

    private Map<UUID, List<Runnable>> scheduledActions = new HashMap<>();


    public void registerDefaults(){
        CobbleContestsFabric.LOGGER.info("Registering Defaults");
        this.register(CobbleContestsMoves.INSTANCE);

        //Cobblemon.implementation.registerResourceReloader(cobblemonResource("client_resources"), new SimpleResourceReloader(PackType.CLIENT_RESOURCES), PackType.CLIENT_RESOURCES, emptyList());

        //Cobblemon.implementation.registerResourceReloader(cobblemonResource("data_resources"), new SimpleResourceReloader(PackType.SERVER_DATA), PackType.SERVER_DATA, emptyList());

        Cobblemon.implementation.registerResourceReloader(cobblemonResource("data_resources"), new SimpleResourceReloader(PackType.SERVER_DATA), PackType.SERVER_DATA, emptyList());
        //IdentifiableResourceReloadListener
        //ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new CobblemonReloadListener(cobblemonResource("data_resources"), new SimpleResourceReloader(PackType.SERVER_DATA), emptyList()));

    }


    @Override
    public void doAfterSync(@NotNull ServerPlayer serverPlayer, @NotNull Function0<Unit> function0) {

    }

    @Nullable
    @Override
    public DataRegistry fromIdentifier(@NotNull ResourceLocation registryIdentifier) {
        return this.registries.stream()
                .filter(registry -> registry.getId().equals(registryIdentifier))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @Override
    public <T extends DataRegistry> T register(@NotNull T registry) {
        if (this.registries.isEmpty()) {
            //CobbleContestsForge.LOGGER.info("Note: Hello.");
        }
        this.registries.add(registry);
        CobbleContestsFabric.LOGGER.info("Registered the {} registry", registry.getId().toString());
        CobbleContestsFabric.LOGGER.debug("Registered the {} registry of class {}", registry.getId().toString(), registry.getClass().getCanonicalName());
        return registry;
    }

    @Override
    public void sync(@NotNull ServerPlayer serverPlayer) {

    }


    private class SimpleResourceReloader implements ResourceManagerReloadListener {
        PackType type;

        public SimpleResourceReloader(PackType type) {
            this.type = type;
        }

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            // Check for a server running, this is due to the create a world screen triggering datapack reloads, these are fine to happen as many times as needed as players may be in the process of adding their datapacks.
            boolean isInGame = server() != null;
            if (isInGame && this.type == PackType.SERVER_DATA && !canReload) {
                return;
            }

            for (DataRegistry registry : registries) {
                if (registry.getType() == this.type) {
                    registry.reload(manager);
                }
            }

            if (isInGame && this.type ==  PackType.SERVER_DATA) {
                canReload = false;
            }
        }
    }



// Taken from cobblemon code because I do not understand it
    /**private class CobblemonReloadListener implements IdentifiableResourceReloadListener {
     private final ResourceLocation identifier;
     private final SimpleResourceReloader reloader;
     private final Collection<ResourceLocation> dependencies;

     public CobblemonReloadListener(ResourceLocation identifier, SimpleResourceReloader reloader, Collection<ResourceLocation> dependencies) {
     this.identifier = identifier;
     this.reloader = reloader;
     this.dependencies = dependencies;
     }

     @Override
     public ResourceLocation getFabricId() {
     return this.identifier;
     }

     @Override
     public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager, ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
     return this.reloader.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
     }

     @Override
     public String getName() {
     return this.reloader.getName();
     }

     @Override
     public Collection<ResourceLocation> getFabricDependencies() {
     return new ArrayList<>(this.dependencies);
     }
     */
}
