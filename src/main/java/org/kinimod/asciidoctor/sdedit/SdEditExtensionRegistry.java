/**
 * 
 */
package org.kinimod.asciidoctor.sdedit;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.extension.spi.ExtensionRegistry;

/**
 * @author Dominik
 *
 */
public final class SdEditExtensionRegistry implements ExtensionRegistry {

	/**
	 * Default constructor
	 */
	public SdEditExtensionRegistry() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.asciidoctor.extension.spi.ExtensionRegistry#register(org.asciidoctor
	 * .Asciidoctor)
	 */
	@Override
	public void register(Asciidoctor asciidoctor) {
		JavaExtensionRegistry javaExtensionRegistry = asciidoctor
				.javaExtensionRegistry();
		javaExtensionRegistry.block(SdEditBlockProcessor.SDEDIT_BLOCK_NAME,
				SdEditBlockProcessor.class);
		javaExtensionRegistry.blockMacro(
				SdEditBlockMacroProcessor.SDEDIT_BLOCK_MACRO_NAME,
				SdEditBlockMacroProcessor.class);
	}

}
