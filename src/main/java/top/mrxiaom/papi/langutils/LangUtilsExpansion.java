package top.mrxiaom.papi.langutils;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.lang.LanguageHelper;
import com.meowj.langutils.locale.LocaleHelper;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class LangUtilsExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "langutils";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MrXiaoM";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "LangUtils";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().isPluginEnabled("LangUtils");
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        Player p = player.getPlayer();
        String lang = p == null ? LangUtils.plugin.config.getString("FallbackLanguage", "en_us") : LocaleHelper.getPlayerLanguage(p);
        if (lang == null) lang = "en_us";
        String args = PlaceholderAPI.setBracketPlaceholders(player, params);
        if (args.startsWith("material_")) {
            args = args.substring("material_".length());
            Material material = valueOf(Material.class, args).orElse(null);

            if (material != null) return LanguageHelper.getItemName(new ItemStack(material), lang);
        }
        else if (args.startsWith("entity_")) {
            args = args.substring("entity_".length());
            EntityType entity = valueOf(EntityType.class, args).orElse(null);

            if (entity != null) return LanguageHelper.getEntityName(entity, lang);
        }
        else if (args.startsWith("enchantment_")) {
            args = args.substring("enchantment_".length());
            Enchantment enchantment = valueOf(Enchantment.values(), it -> it.getKey().getKey(), args).orElse(null);

            if (enchantment != null) return LanguageHelper.getEnchantmentName(enchantment, lang);
        }
        else if (args.startsWith("level_")) {
            try {
                int level = Integer.parseInt(args.substring("level_".length()));
                return LanguageHelper.getEnchantmentLevelName(level, lang);
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    public static <T extends Enum<T>> Optional<T> valueOf(Class<T> clazz, String s) {
        return valueOf(clazz.getEnumConstants(), Enum::name, s);
    }
    public static <T> Optional<T> valueOf(T[] values, Function<T, String> func, String s) {
        return Arrays.stream(values).filter(it -> func.apply(it).equalsIgnoreCase(s)).findFirst();
    }
}
