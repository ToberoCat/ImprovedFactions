import React from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
    title: string;
    Img: string;
    description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
    {
        title: 'What is Improved Factions? 🏰',
        Img: require('@site/static/img/what_is_factions.png').default,
        description: (
            <>
                Improved Factions is a modern factions plugin designed to enhance the Minecraft factions experience for both server owners and players. It offers a seamless setup out of the box and is highly customizable to suit your server's needs.
            </>
        ),
    },
    {
        title: 'Why Choose Improved Factions? ✨',
        Img: require('@site/static/img/why_choose.png').default,
        description: (
            <>
                With a wide range of features and options, Improved Factions provides a unique and engaging factions experience. It offers modern GUIs, extensive customization, PAPI support, and much more to keep players coming back for more.
            </>
        ),
    },
    {
        title: 'Key Features 🚀',
        Img: require('@site/static/img/features.png').default,
        description: (
            <div style={{ textAlign: 'center' }}>
                    <p>Modern GUIs</p>
                    <p>Extremely customizable</p>
                    <p>PAPI support</p>
                    <p>Easily enable or disable features</p>
                    <p>Dynmap support</p>
                    <p>Advanced power system</p>
                    <p>Player customizable factions</p>
                    <p>Custom permission management</p>
                    <p>Advanced claim system (raids, etc.)</p>
                    <p>Integrated wilderness</p>
                    <p>Fully translatable</p>
                    <p>Permissions</p>
                    <p>And much more!</p>
            </div>
        ),
    },
    {
        title: 'Get Started Today! 🚀',
        Img: require('@site/static/img/get_started.png').default,
        description: (
            <>
                Ready to take your factions experience to the next level? Download Improved Factions today and start customizing your server! Whether you're a server owner or a player, Improved Factions has something for everyone.
            </>
        ),
    },
    {
        title: 'Need Help? 🤖',
        Img: require('@site/static/img/support.png').default,
        description: (
            <>
                If you need assistance, check out our <a href="https://chatgpt.com/g/g-tvLQ5jbIz-improved-factions-assistant">Factions Assistant</a> chatbot for instant support, or join our <a href="https://discord.com/invite/VmSbFNZejz">Discord server</a> for human support!
            </>
        ),
    },
];

function Feature({ title, Img, description }: FeatureItem) {
    return (
        <div className={clsx('col col--6')}>
            <div className="text--center">
                <img src={Img} className={styles.featureImg} alt={title} />
            </div>
            <div className="text--center padding-horiz--md">
                <Heading as="h3">{title}</Heading>
                <p>{description}</p>
            </div>
        </div>
    );
}

export default function HomepageFeatures(): JSX.Element {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}